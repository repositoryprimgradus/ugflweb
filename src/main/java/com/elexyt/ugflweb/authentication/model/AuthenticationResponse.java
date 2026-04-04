package com.elexyt.ugflweb.authentication.model;

public class AuthenticationResponse {

	private String token;
    private String roleName;
	
	public AuthenticationResponse()
	{

	}

	public AuthenticationResponse(String token, String roleName) {
		super();
		this.token = token;
        this.roleName = roleName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

	
}
