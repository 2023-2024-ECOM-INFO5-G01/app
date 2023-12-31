package fr.uga.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, fr.uga.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, fr.uga.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, fr.uga.domain.User.class.getName());
            createCache(cm, fr.uga.domain.Authority.class.getName());
            createCache(cm, fr.uga.domain.User.class.getName() + ".authorities");
            createCache(cm, fr.uga.domain.EPA.class.getName());
            createCache(cm, fr.uga.domain.Ehpad.class.getName());
            createCache(cm, fr.uga.domain.Ehpad.class.getName() + ".users");
            createCache(cm, fr.uga.domain.Ehpad.class.getName() + ".patients");
            createCache(cm, fr.uga.domain.Patient.class.getName());
            createCache(cm, fr.uga.domain.Patient.class.getName() + ".users");
            createCache(cm, fr.uga.domain.Patient.class.getName() + ".poids");
            createCache(cm, fr.uga.domain.Patient.class.getName() + ".ePAS");
            createCache(cm, fr.uga.domain.Patient.class.getName() + ".iMCS");
            createCache(cm, fr.uga.domain.Patient.class.getName() + ".repas");
            createCache(cm, fr.uga.domain.Patient.class.getName() + ".rappels");
            createCache(cm, fr.uga.domain.Patient.class.getName() + ".notes");
            createCache(cm, fr.uga.domain.Albumine.class.getName());
            createCache(cm, fr.uga.domain.Rappel.class.getName());
            createCache(cm, fr.uga.domain.Poids.class.getName());
            createCache(cm, fr.uga.domain.IMC.class.getName());
            createCache(cm, fr.uga.domain.Repas.class.getName());
            createCache(cm, fr.uga.domain.Repas.class.getName() + ".aliments");
            createCache(cm, fr.uga.domain.Note.class.getName());
            createCache(cm, fr.uga.domain.Aliment.class.getName());
            createCache(cm, fr.uga.domain.Alerte.class.getName());
            createCache(cm, fr.uga.domain.Rappel.class.getName() + ".users");
            createCache(cm, fr.uga.domain.Alerte.class.getName() + ".users");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
