package finalauthentication.auth.config;


public final class Paths {
	private Paths() {
		
	}
	protected static final String[] EXCLUDED_PATHS={"/actuator/**","/authenticate", "/authorize","/swagger-ui.html","/swagger-resources","/v2/api-docs","/webjars/**","/**"};
	protected static final String[] REDIRECT_PATHS={"/swagger-ui.html"};
}
