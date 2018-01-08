package site.fish119.adminss.repository;

import io.swagger.annotations.Api;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import site.fish119.adminss.domain.SysUser;

@RepositoryRestResource
@Api
public interface SysUserRepository extends PagingAndSortingRepository<SysUser, Long> {
    @Cacheable(cacheNames = "sysUsers", key = "#p0")
    SysUser findByUsername(@Param("username") final String username);

    @Override
    SysUser findOne(Long id);

    @Override
    @Caching(
            evict = {@CacheEvict(cacheNames = "sysUsers", key = "#root.caches[0].get('useridAndName:'+#p0.id).get().username",beforeInvocation = true,condition ="#root.caches[0].get('useridAndName:'+#p0.id) != null" )},
            put = {
                    @CachePut(cacheNames = "sysUsers", key = "'useridAndName:'+#result.id", condition = "#result!=null"),
                    @CachePut(cacheNames = "sysUsers", key = "#result.username", condition = "#result!=null")
            }
    )
    SysUser save(SysUser user);
}