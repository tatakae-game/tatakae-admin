package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Permission;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.util.ArrayList;

public class PermissionService {

    public static ArrayList<Permission> serialize(JSONArray jPermissions) {
        ArrayList<Permission> permissions = new ArrayList<>();

        for (int i = 0; i < jPermissions.length(); i++) {
            permissions.add(serialize(jPermissions.getJSONObject(i)));
        }

        return permissions;
    }

    public static Permission serialize(JSONObject jPermission) {
        if (jPermission.has("_id") && jPermission.has("name") && jPermission.has("value")) {
            var id = jPermission.getString("_id");
            var name = jPermission.getString("name");
            var value = jPermission.getBoolean("value");
            return new Permission(id, name, value);
        }
        return null;
    }
}
