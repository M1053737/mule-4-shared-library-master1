package com.mulesoft

public class Constants {
    public static final JENKINS_DOMAIN = "192.168.178.66:9090"
    public static final JENKINS_API_TOKEN = "CICD-pipeline" // This is a token now
    public static final MULE_DOMAIN = "mule-platform-api.de-c1.cloudhub.io"
   
    public static final GH_CREDENTIALS_ID = "gitlab"
    public static final POM_GROUPID_PREFIX= "mule-demo"

    public static final PROPERTY_FILE_NAMES = ["Development":'dev', "Test":'tst']
    public static final MAVEN_GLOBAL_SETTINGS = "global_maven_settings"

   public static final NEXUS_SNAPSHOTS_REPO_ID = "com-project-snapshots"
    public static final NEXUS_SNAPSHOTS_REPO_URL = "https://nexus.project.com/repository/project-snapshots/"
    public static final NEXUS_RELEASES_REPO_ID = "com-project-releases"
    public static final NEXUS_RELEASES_REPO_URL = "https://nexus.project.com/repository/project-releases/"


    public static final ANYPOINT_URL = "https://anypoint.mulesoft.com"
    public static final ANYPOINT_CREDENTIALS_ID = "anypoint"

    public static final ROOT_ORGANIZATION = "Test" // This should be dynamic

    public static final FQN_PREFIX = "com.mule.code.generator"


   
}
