package com.example.runner;

import java.security.AllPermission;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.Provider;

public final class PermissivePolicy extends Policy {
	private final Permissions defaultPermissions;

	/**
	 * Construct a new policy.
	 */

	public PermissivePolicy() {
		final Permissions q = new Permissions();
		q.add(new AllPermission());
		q.setReadOnly();
		this.defaultPermissions = q;
	}

	@Override
	public Provider getProvider() {
		return null;
	}

	@Override
	public void refresh() {
		// Nothing
	}

	@Override
	public PermissionCollection getPermissions(final CodeSource codesource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PermissionCollection getPermissions(final ProtectionDomain domain) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public Parameters getParameters() {
		return null;
	}

	@Override
	public boolean implies(final ProtectionDomain domain, final Permission permission) {
		return this.defaultPermissions.implies(permission);
	}
}