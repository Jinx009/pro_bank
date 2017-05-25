package com.rongdu.common.util.des;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

public class DecryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
	private Resource[] locations;
	private Resource keyLocation;
	private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	private String fileEncoding = "utf-8";
	private boolean ignoreResourceNotFound = false;

	@Override
	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	@Override
	public void setFileEncoding(String encoding) {
		this.fileEncoding = encoding;
	}

	@Override
	public void setIgnoreResourceNotFound(boolean ignoreResourceNotFound) {
		this.ignoreResourceNotFound = ignoreResourceNotFound;
	}

	public void setKeyLocation(Resource keyLocation) {
		this.keyLocation = keyLocation;
	}

	@Override
	/**
	 * Load properties into the given instance.
	 * @param props the Properties instance to load into
	 * @throws java.io.IOException in case of I/O errors
	 * @see #setLocations
	 */
	protected void loadProperties(Properties props) throws IOException {
		if (this.locations != null) {
			for (int i = 0; i < this.locations.length; i++) {
				Resource location = this.locations[i];
				InputStream is = null; // 属性文件输入流
				InputStream keyStream = null; // 密钥输入流
				InputStream readIs = null; // 解密后属性文件输入流
				try {
					// 属性文件输入流
					is = location.getInputStream();
					// 密钥输入流
					keyStream = keyLocation.getInputStream();
					// 得到解密后的输入流对象
					readIs = DesUtil.decrypt(is, DesUtil.getKey(keyStream));
					// 以下操作按照Spring的流程做即可
					if (location.getFilename().endsWith(XML_FILE_EXTENSION)) {
						this.propertiesPersister.loadFromXml(props, readIs);
					} else {
						if (this.fileEncoding != null) {
							this.propertiesPersister.load(props, new InputStreamReader(readIs, this.fileEncoding));
						} else {
							this.propertiesPersister.load(props, readIs);
						}
					}
				} catch (Exception ex) {
					if (this.ignoreResourceNotFound) {
						if (logger.isWarnEnabled()) {
							logger.warn("Could not load properties from " + location + ": " + ex.getMessage());
						}
					}
				} finally {
					IOUtils.closeQuietly(is);
					IOUtils.closeQuietly(keyStream);
					IOUtils.closeQuietly(readIs);
				}
			}
		}
	}
}
