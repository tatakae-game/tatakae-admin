package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Exceptions.FailedParsingJsonException;
import com.tatakae.admin.core.Group;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

import java.util.ArrayList;

public class GroupService {

    public static ArrayList<Group> serialize(JSONArray jGroups) throws FailedParsingJsonException{
        try {
            ArrayList<Group> groups = new ArrayList<>();

            for (int i = 0; i < jGroups.length(); i++) {
                groups.add(serialize(jGroups.getJSONObject(i)));
            }

            return groups;

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Group list: " + e.getMessage());
        }
    }

    public static Group serialize(JSONObject jGroup) throws FailedParsingJsonException {
        try {
            var id = jGroup.getString("_id");
            var name = jGroup.getString("name");
            var permissions = PermissionService.serialize(jGroup.getJSONArray("permissions"));

            return new Group(id, name, permissions);

        } catch (Exception e) {
            throw new FailedParsingJsonException(
                    "Failed to parse JSON Group: " + e.getMessage());
        }
    }
}
