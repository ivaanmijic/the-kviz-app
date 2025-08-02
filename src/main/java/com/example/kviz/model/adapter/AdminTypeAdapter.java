package com.example.kviz.model.adapter;

import com.example.kviz.model.Admin;
import com.example.kviz.model.supporting.AdminRole;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class AdminTypeAdapter extends TypeAdapter<Admin> {
    @Override
    public void write(JsonWriter out, Admin value) throws IOException {
        out.beginObject();
        out.name("id").value(value.getId());
        out.name("email").value(value.getEmail());
        out.name("username").value(value.getUsername());
        if (value.getRole() != null) {
            out.name("role").value(value.getRole().toString());
        }
        if (value.getCreatedAt() != null) {
            out.name("createdAt").value(value.getCreatedAt().toString());
        }
        if (value.getUpdatedAt() != null) {
            out.name("updatedAt").value(value.getUpdatedAt().toString());
        }
        out.endObject();
    }

    @Override
    public Admin read(JsonReader in) throws IOException {
        Admin admin = new Admin();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "id":
                    admin.setId(in.nextLong());
                    break;
                case "email":
                    admin.setEmail(in.nextString());
                    break;
                case "username":
                    admin.setUsername(in.nextString());
                    break;
                case "password":
                    admin.setPassword(in.nextString());
                    break;
                case "role":
                    admin.setRole(AdminRole.valueOf(in.nextString()));
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return admin;
    }
}