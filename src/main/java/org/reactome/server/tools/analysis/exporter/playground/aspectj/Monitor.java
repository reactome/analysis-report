package org.reactome.server.tools.analysis.exporter.playground.aspectj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Chuan-Deng dengchuanbio@gmail.com
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Monitor {
}