/**
 *  Copyright 2011 Terracotta, Inc.
 *  Copyright 2011 Oracle America Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.jsr107.ri;

import java.util.ArrayList;

import javax.cache.CacheConfiguration.Duration;
import javax.cache.CacheConfiguration.ExpiryType;
import javax.cache.CacheLoader;
import javax.cache.CacheWriter;
import javax.cache.event.CacheEntryListener;
import javax.cache.transaction.IsolationLevel;
import javax.cache.transaction.Mode;

/**
 * A base {@link CacheConfigurationBuilder}.
 * 
 * @param <K> the type of keys maintained the cache
 * @param <V> the type of cached values
 * @param <B> the builder type
 * 
 * @author Brian Oliver
 * @since 1.0
 */
public abstract class AbstractCacheConfigurationBuilder<K, V, B extends CacheConfigurationBuilder<K, V, ? extends B>> 
    implements CacheConfigurationBuilder<K, V, B> {

    private static final ExpiryType DEFAULT_EXPIRY_TYPE = ExpiryType.MODIFIED;
    private static final Duration DEFAULT_EXPIRY_TIME_TO_LIVE = Duration.ETERNAL;
    private static final boolean DEFAULT_IS_READ_THROUGH = false;
    private static final boolean DEFAULT_IS_WRITE_THROUGH = false;
    private static final boolean DEFAULT_IS_STATISTICS_ENABLED = false;
    private static final boolean DEFAULT_STORE_BY_VALUE = true;
    private static final IsolationLevel DEFAULT_TRANSACTION_ISOLATION_LEVEL = IsolationLevel.NONE;
    private static final Mode DEFAULT_TRANSACTION_MODE = Mode.NONE;

    /**
     * The collection of {@link CacheEntryListener}s to register when building
     * a CacheConfiguration.
     */
    protected ArrayList<CacheEntryListener<? super K, ? super V>> cacheEntryListeners;

    /**
     * The {@link CacheLoader} for the built CacheConfiguration.
     */
    protected CacheLoader<K, ? extends V> cacheLoader;
    
    /**
     * The {@link CacheWriter} for the build CacheConfiguration.
     */
    protected CacheWriter<? super K, ? super V> cacheWriter;

    /**
     * A flag indicating if "read-through" mode is required.
     */
    protected boolean isReadThrough = DEFAULT_IS_READ_THROUGH;
    
    /**
     * A flag indicating if "write-through" mode is required.
     */
    protected boolean isWriteThrough = DEFAULT_IS_WRITE_THROUGH;
    
    /**
     * A flag indicating if statistics gathering is enabled.
     */
    protected boolean isStatisticsEnabled = DEFAULT_IS_STATISTICS_ENABLED;

    /**
     * A flag indicating if the cache will be store-by-value or store-by-reference.
     */
    protected boolean storeByValue;
    
    /**
     * The expiration time-to-live {@link Duration}.
     */
    protected Duration expiryDuration;

    /**
     * The type of expiry.
     */
    protected ExpiryType expiryType;
    
    /**
     * The transaction {@link IsolationLevel}.
     */
    protected IsolationLevel txnIsolationLevel;

    /**
     * The transaction {@link Mode}.
     */
    protected Mode txnMode;
    
    /**
     * Constructs an {@link AbstractCacheConfigurationBuilder} using the
     * default CacheConfiguration options.
     */
    public AbstractCacheConfigurationBuilder() {
        this.cacheEntryListeners = new ArrayList<CacheEntryListener<? super K, ? super V>>();
        this.cacheLoader = null;
        this.cacheWriter = null;
        this.expiryType = DEFAULT_EXPIRY_TYPE;
        this.expiryDuration = DEFAULT_EXPIRY_TIME_TO_LIVE;
        this.isReadThrough = DEFAULT_IS_READ_THROUGH;
        this.isWriteThrough = DEFAULT_IS_WRITE_THROUGH;
        this.isStatisticsEnabled = DEFAULT_IS_STATISTICS_ENABLED;
        this.storeByValue = DEFAULT_STORE_BY_VALUE;
        this.txnIsolationLevel = DEFAULT_TRANSACTION_ISOLATION_LEVEL;
        this.txnMode = DEFAULT_TRANSACTION_MODE;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public B addCacheEntryListener(CacheEntryListener<? super K, ? super V> cacheEntryListener) {
        cacheEntryListeners.add(cacheEntryListener);
        return (B)this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public B setCacheLoader(CacheLoader<K, ? extends V> cacheLoader) {
        this.cacheLoader = cacheLoader;
        return (B)this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public B setCacheWriter(CacheWriter<? super K, ? super V> cacheWriter) {
        this.cacheWriter = cacheWriter;
        return (B)this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public B setExpiry(ExpiryType expiryType, Duration duration) {
        if (expiryType == null) {
            throw new NullPointerException("expiryType can not be null");
        }
        
        if (duration == null) {
            throw new NullPointerException("duration can not be null");
        }
            
        this.expiryType = expiryType;
        this.expiryDuration = duration;
        return (B)this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public B setReadThrough(boolean isReadThrough) {
        this.isReadThrough = isReadThrough;
        return (B)this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public B setWriteThrough(boolean isWriteThrough) {
        this.isWriteThrough = isWriteThrough;
        return (B)this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public B setStatisticsEnabled(boolean isStatisticsEnabled) {
        this.isStatisticsEnabled = isStatisticsEnabled;
        return (B)this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public B setStoreByValue(boolean storeByValue) {
        this.storeByValue = storeByValue;
        return (B)this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public B setTransactions(IsolationLevel isolationLevel, Mode mode) {
        this.txnIsolationLevel = isolationLevel;
        this.txnMode = mode;
        return (B)this;
    }
}