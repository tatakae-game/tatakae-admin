package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Group;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.util.ArrayList;

public class GroupService {

    public static ArrayList<Group> serialize(JSONArray jGroups) {
        ArrayList<Group> groups = new ArrayList<>();

        for (int i = 0; i < jGroups.length(); i++) {
            groups.add(serialize(jGroups.getJSONObject(i)));
        }

        return groups;
    }

    public static Group serialize(JSONObject jGroup) {
        if (jGroup.has("_id") && jGroup.has("name") && jGroup.has("permissions")) {
            var id = jGroup.getString("_id");
            var name = jGroup.getString("name");
            var permissions = PermissionService.serialize(jGroup.getJSONArray("permissions"));

            return new Group(id, name, permissions);
        }
        return null;
    }
}
