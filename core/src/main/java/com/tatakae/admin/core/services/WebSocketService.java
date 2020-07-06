package com.tatakae.admin.core.services;

import com.tatakae.admin.core.Config;
import com.tatakae.admin.core.Exceptions.CannotCreateFileException;
import com.tatakae.admin.core.LocalDataManager;
import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URISyntaxException;
import java.util.Map;
import java.util.stream.Collectors;

public class WebSocketService {

    public static Socket connect(final String path, final Map<String, String> query)
            throws URISyntaxException {
        try {
            final var url = Config.url.concat(path);
            final var queries = query.entrySet().stream()
                    .map((entry) -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));

            final var queryString = (!queries.isEmpty()) ? "&" + queries : "";
            final var queryToken = "?token=" + LocalDataManager.getToken();

            return IO.socket(url.concat(queryToken.concat(queryString)));

        } catch (URISyntaxException e) {
            throw new URISyntaxException(e.getMessage(), e.getReason());
        }
    }
}
