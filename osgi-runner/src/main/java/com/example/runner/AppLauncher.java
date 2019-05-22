package com.example.runner;

import org.apache.felix.framework.util.FelixConstants;
import org.apache.felix.main.AutoProcessor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Policy;
import java.util.HashMap;
import java.util.Map;


public class AppLauncher {

	static {
		Policy.setPolicy(new PermissivePolicy());
	}

	private static final Logger LOG = LoggerFactory.getLogger(AppLauncher.class);

	private Framework fwk;

	public void launch() {
		try {
			AppLauncher main = new AppLauncher();
			main.launchFelixPlatform();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	private void launchFelixPlatform() {
		LOG.info("Starting felix platform");
		try {

			FrameworkFactory frameworkFactory = getFrameworkFactory();
			if (frameworkFactory == null) {
				LOG.error("framework Factory is null");
				return;
			}

			Map<String, String> configMap = new HashMap<>();
			configMap.put(FelixConstants.LOG_LEVEL_PROP, "999");
			configMap.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA, getExtraFwkPackages());
			configMap.put(Constants.FRAMEWORK_STORAGE_CLEAN, "onFirstInit");
			configMap.put(Constants.FRAMEWORK_STORAGE, "cache");
			configMap.put("felix.auto.deploy.dir", "system-bundles");
			configMap.put("felix.auto.deploy.action", "install,start,update");
			configMap.put("felix.fileinstall.dir", "bundles");
			configMap.put("felix.fileinstall.poll", "4000");
			configMap.put("felix.fileinstall.log.level", "3");
			configMap.put("felix.fileinstall.bundles.new.start", "true");
			configMap.put("felix.fileinstall.bundles.startTransient", "true");
			configMap.put("ds.loglevel","3");

			fwk = frameworkFactory.newFramework(configMap);
			fwk.init();
			AutoProcessor.process(configMap, fwk.getBundleContext());
			fwk.start();
			LOG.info("Started");
			fwk.waitForStop(0);
			System.exit(0);

		} catch (Exception ex) {
			LOG.error("Could not create framework: {}", ex);
			System.exit(-1);
		}
	}

	private FrameworkFactory getFrameworkFactory()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String fwkFactory = "org.apache.felix.framework.FrameworkFactory";
		return (FrameworkFactory) Class.forName(fwkFactory).newInstance();
	}

	private String getExtraFwkPackages() {
		final StringBuilder sb = new StringBuilder(128);
		sb.append("org.slf4j; version=1.7.25");
		sb.append(",");
		sb.append("org.slf4j.impl; version=1.7.25");
		sb.append(",");
		sb.append("org.slf4j.spi; version=1.7.25");
		sb.append(",");
		sb.append("org.slf4j.helpers; version=1.7.25");
		sb.append(",");
		sb.append("org.slf4j.event; version=1.7.25");
		return sb.toString();
	}

	public static void main(String[] args) {
		AppLauncher launcher = new AppLauncher();
		launcher.launch();
	}

}
