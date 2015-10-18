package org.easy4j.framework.web.startup;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author: liuyong
 * @since 1.0
 */
public class AnnotationAndXmlConfigWebApplicationContext extends AnnotationConfigWebApplicationContext {

    private final Set<Class<?>> annotatedClasses = new LinkedHashSet<Class<?>>();

    private final Set<String> basePackages = new LinkedHashSet<String>();

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        loadAnnotationBeanDefinitions(beanFactory);
        loadXmlBeanDefinitions(beanFactory);
    }

    private void loadAnnotationBeanDefinitions(DefaultListableBeanFactory beanFactory){

        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
        reader.setEnvironment(getEnvironment());

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
        scanner.setEnvironment(getEnvironment());

        BeanNameGenerator beanNameGenerator = getBeanNameGenerator();
        ScopeMetadataResolver scopeMetadataResolver = getScopeMetadataResolver();
        if (beanNameGenerator != null) {
            reader.setBeanNameGenerator(beanNameGenerator);
            scanner.setBeanNameGenerator(beanNameGenerator);
            beanFactory.registerSingleton(AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR, beanNameGenerator);
        }
        if (scopeMetadataResolver != null) {
            reader.setScopeMetadataResolver(scopeMetadataResolver);
            scanner.setScopeMetadataResolver(scopeMetadataResolver);
        }

        if (!this.annotatedClasses.isEmpty()) {
            if (logger.isInfoEnabled()) {
                logger.info("Registering annotated classes: [" +
                        StringUtils.collectionToCommaDelimitedString(this.annotatedClasses) + "]");
            }
            reader.register(this.annotatedClasses.toArray(new Class<?>[this.annotatedClasses.size()]));
        }

        if (!this.basePackages.isEmpty()) {
            if (logger.isInfoEnabled()) {
                logger.info("Scanning base packages: [" +
                        StringUtils.collectionToCommaDelimitedString(this.basePackages) + "]");
            }
            scanner.scan(this.basePackages.toArray(new String[this.basePackages.size()]));
        }
    }

    /**
     * Register one or more annotated classes to be processed.
     * <p>Note that {@link #refresh()} must be called in order for the context
     * to fully process the new classes.
     * @param annotatedClasses one or more annotated classes,
     * e.g. {@link org.springframework.context.annotation.Configuration @Configuration} classes
     * @see #scan(String...)
     * @see #loadBeanDefinitions(DefaultListableBeanFactory)
     * @see #setConfigLocation(String)
     * @see #refresh()
     */
    public void register(Class<?>... annotatedClasses) {
        Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
        this.annotatedClasses.addAll(Arrays.asList(annotatedClasses));
    }

    /**
     * Perform a scan within the specified base packages.
     * <p>Note that {@link #refresh()} must be called in order for the context
     * to fully process the new classes.
     * @param basePackages the packages to check for annotated classes
     * @see #loadBeanDefinitions(DefaultListableBeanFactory)
     * @see #register(Class...)
     * @see #setConfigLocation(String)
     * @see #refresh()
     */
    public void scan(String... basePackages) {
        Assert.notEmpty(basePackages, "At least one base package must be specified");
        this.basePackages.addAll(Arrays.asList(basePackages));
    }


    private void loadXmlBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // Create a new XmlBeanDefinitionReader for the given BeanFactory.
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

        // Configure the bean definition reader with this context's
        // resource loading environment.
        beanDefinitionReader.setEnvironment(getEnvironment());
        beanDefinitionReader.setResourceLoader(this);
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

        // Allow a subclass to provide custom initialization of the reader,
        // then proceed with actually loading the bean definitions.
        initBeanDefinitionReader(beanDefinitionReader);
        loadBeanDefinitions(beanDefinitionReader);
    }

    /**
     * Load the bean definitions with the given XmlBeanDefinitionReader.
     * <p>The lifecycle of the bean factory is handled by the refreshBeanFactory method;
     * therefore this method is just supposed to load and/or register bean definitions.
     * <p>Delegates to a ResourcePatternResolver for resolving location patterns
     * into Resource instances.
     * @throws java.io.IOException if the required XML document isn't found
     * @see #refreshBeanFactory
     * @see #getConfigLocations
     * @see #getResources
     * @see #getResourcePatternResolver
     */
    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) {
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            for (String configLocation : configLocations) {
                reader.loadBeanDefinitions(configLocation);
            }
        }
    }

    /**
     * Initialize the bean definition reader used for loading the bean
     * definitions of this context. Default implementation is empty.
     * <p>Can be overridden in subclasses, e.g. for turning off XML validation
     * or using a different XmlBeanDefinitionParser implementation.
     * @param beanDefinitionReader the bean definition reader used by this context
     * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader#setValidationMode
     * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader#setDocumentReaderClass
     */
    protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
    }


}
