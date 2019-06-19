package edu.soumya.spring.sqldiff.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import edu.soumya.spring.sqldiff.plugin.constants.Constants;
import edu.soumya.spring.sqldiff.plugin.runner.SqlDiffRunner;

/**
 * @author soumyab
 *
 */
@Mojo(name="run")
@SpringBootApplication
public class SqlDiffMojo
    extends AbstractMojo implements ApplicationRunner {
	@Autowired
	SqlDiffRunner diffRunner;
	
	@Parameter(property="project",defaultValue="${project}")
	MavenProject project;
	
	@Parameter(property="buildForOnly")
	String buildForOnly;
	
	@Parameter(property = "config.location",required=true)
	private String configLocation;
	
	@Parameter(property = "diff.skip",defaultValue="false")
	private String skip;
	
    public void execute()
            throws MojoExecutionException {
    	if(!BooleanUtils.toBoolean(skip) && (StringUtils.isBlank(buildForOnly) || StringUtils.equalsIgnoreCase(project.getName(), buildForOnly))) {
	    	getLog().info(skip);
    		getLog().info("Config Name: " + getConfigNameOption());
	    	getLog().info("Config Location: " + getConfigLocationOption());
	    	getLog().info(project.getName());
			new SpringApplicationBuilder(SqlDiffMojo.class)
					.properties(getConfigNameOption(), getConfigLocationOption())
					.build().run(StringUtils.EMPTY);
		}
    }
    
    /**
	 * @return the project
	 */
	public MavenProject getProject() {
		return project;
	}
	/**
	 * @param project the project to set
	 */
	public void setProject(MavenProject project) {
		this.project = project;
	}
    
	/**
	 * @return the buildForOnly
	 */
	public String getBuildForOnly() {
		return buildForOnly;
	}

	/**
	 * @param buildForOnly the buildForOnly to set
	 */
	public void setBuildForOnly(String buildForOnly) {
		this.buildForOnly = buildForOnly;
	}

	/**
	 * @return the configLocation
	 */
	public String getConfigLocation() {
		return configLocation;
	}
	/**
	 * @param configLocation the configLocation to set
	 */
	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}
    
	/**
	 * @return the skip
	 */
	public String getSkip() {
		return skip;
	}

	/**
	 * @param skip the skip to set
	 */
	public void setSkip(String skip) {
		this.skip = skip;
	}
	
	@Override
    public void run(ApplicationArguments args) throws Exception {
		diffRunner.execute();
	}
	
	private String getConfigNameOption() {
		return "spring.config.name:" + Constants.FILE_LOC_CONFIG_NAME
				+ Constants.COMMA_SEPERATOR + Constants.DATASOURCE_CONFIG_NAME
				+ Constants.COMMA_SEPERATOR + Constants.LOGBACK_CONFIG_NAME
				+ Constants.COMMA_SEPERATOR + Constants.SQL_FORMAT_CONFIG_NAME;
	}
	
	private String getConfigLocationOption() {
		return "spring.config.location:" + getConfigFileFullPath(Constants.FILE_LOC_CONFIG_FILE_NAME)
				+ Constants.COMMA_SEPERATOR + getConfigFileFullPath(Constants.DATASOURCE_CONFIG_FILE_NAME)
				+ Constants.COMMA_SEPERATOR + getConfigFileFullPath(Constants.LOGBACK_CONFIG_FILE_NAME)
				+ Constants.COMMA_SEPERATOR + getConfigFileFullPath(Constants.SQL_FORMAT_CONFIG_FILE_NAME);
	}
	
	private String getConfigFileFullPath(String fileName) {
		Path fullConfigLocPath = Paths.get(getConfigLocation(),fileName);
		return fullConfigLocPath.toString();
	}
	
}
