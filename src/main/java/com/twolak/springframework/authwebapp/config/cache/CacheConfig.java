package com.twolak.springframework.authwebapp.config.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author twolak
 */
@EnableCaching
@Configuration
public class CacheConfig {
	
    //FOR SPRING
//    @Bean
//    public CacheManager cacheManager() {
//        EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching.getCachingProvider();
//
//        Map<String, CacheConfiguration<?, ?>> caches = new HashMap<>();
//        caches.put(Globals.Caches.USERS_CACHE, getCache());
//        caches.put(Globals.Caches.POSTS_CACHE, getCache());
//
//        DefaultConfiguration configuration = new DefaultConfiguration(caches, provider.getDefaultClassLoader());
//        return new JCacheCacheManager(provider.getCacheManager(provider.getDefaultURI(), configuration));
//    }
//    
//	private CacheConfiguration<?, ?> getCache() {
//		return CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class, 
//				ResourcePoolsBuilder.heap(1000).offheap(10, MemoryUnit.MB)/*.disk(20, MemoryUnit.MB, true)*/)
//				.withExpiry(new ExpiryPolicy<Object, Object>() {
//		
//					@Override
//					public Duration getExpiryForCreation(Object key, Object value) {
//						return Duration.ofSeconds(60*60);
//					}
//			
//					@Override
//					public Duration getExpiryForAccess(Object key, Supplier value) {
//						return null;
//					}
//			
//					@Override
//					public Duration getExpiryForUpdate(Object key, Supplier oldValue, Object newValue) {
//						return null;
//					}
//				}).withService(CacheEventListenerConfigurationBuilder.newEventListenerConfiguration(new CustomCacheEventLogger(), EventType.CREATED, 
//    	    		EventType.UPDATED, EventType.EXPIRED, EventType.REMOVED, EventType.EVICTED).unordered().asynchronous())
//				.build();
//	}
}
