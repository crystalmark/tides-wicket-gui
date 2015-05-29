package uk.co.crystalmark;

import java.time.LocalDate;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import uk.co.crystalmark.config.LocalDateConverter;

@Component
@EnableAutoConfiguration
@ComponentScan
public class WicketWebApplication extends WebApplication {

	private final static Logger logger = LoggerFactory
			.getLogger(WicketWebApplication.class);

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * spring boot main method to build context
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(WicketWebApplication.class, args);
	}

	/**
	 * provides page for default request
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

	/**
	 * <ul>
	 * <li>making the wicket components injectable by activating the
	 * SpringComponentInjector</li>
	 * <li>mounting the test page</li>
	 * <li>logging spring service method output to showcase working integration</li>
	 * </ul>
	 */
	@Override
	protected void init() {
		super.init();
		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this, applicationContext));
		getMarkupSettings().setStripWicketTags(true);

//		BootstrapSettings settings = new BootstrapSettings();
//		settings.minify(true); 
//		Bootstrap.install(this, settings);
	}

	@Override
	protected IConverterLocator newConverterLocator() {
		ConverterLocator locator = (ConverterLocator) super
				.newConverterLocator();
		locator.set(LocalDate.class, new LocalDateConverter());
		return locator;
	}
}
