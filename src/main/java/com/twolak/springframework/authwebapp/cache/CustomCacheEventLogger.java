package com.twolak.springframework.authwebapp.cache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

/**
 *
 * @author twolak
 */
public class CustomCacheEventLogger implements CacheEventListener<Object, Object> {

	@Override
	public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent) {
        System.out.println(String.format("Cache event = %1$s, Key = %2$s,  Old value = %3$s, New value = %4$s", 
        		cacheEvent.getType(), cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue()));
	}
}
