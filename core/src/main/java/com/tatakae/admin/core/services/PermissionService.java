package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.Permission;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.util.ArrayList;

public class PermissionService {

    public static ArrayList<Permission> serialize(JSONArray jPermissions) throws FailedParsingJsonException {
        try {
            ArrayList<Permission> permissions = new ArrayList<>();

            for (int i = 0; i < jPermissions.length(); i++) {
                permissions.add(serialize(jPermissions.getJSONObject(i)));
            }

            return permissions;

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Permission list: " + e.getMessage());
        }
    }

    public static Permission serialize(JSONObject jPermission) throws FailedParsingJsonException {
        try {
            var id = jPermission.getString("_id");
            var name = jPermission.getString("name");
            var value = jPermission.getBoolean("value");

            return new Permission(id, name, value);

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Permission: " + e.getMessage());
        }
    }
}
