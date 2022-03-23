/**
 * 
 */
package com.brailsoft.property;

import java.util.logging.Level;

import com.brailsoft.base.ApplicationDecsriptor;

/**
 * @author nevil
 *
 */
public class PropertyDescriptor extends ApplicationDecsriptor {

	private Level loggingLevel = Level.ALL;

	public PropertyDescriptor() {
		super("property.management");
	}

	public PropertyDescriptor(String applicationName) {
		super(applicationName);
	}

	@Override
	public Level level() {
		return loggingLevel;
	}

	@Override
	public String version() {
		return "1.0.0";
	}

	public void setLevel(Level level) {
		if (level == null) {
			throw new IllegalArgumentException("level was null");
		}
		loggingLevel = level;
	}

}
