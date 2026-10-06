package com.ai_agent.developer_assistant;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.apache.logging.log4j.ThreadContext.containsKey;

@Component
public class ProjectTools {

    private static final String PROJECT_ROOT = "src/main/java";


    @Tool(
            name = "listProjectFiles",
            description = "List all the java source file in the project ."
    )
    public List<String> listProjectFiles(){

        try(Stream<Path> paths = Files.walk(Paths.get(PROJECT_ROOT))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error while listing project files", e);
        }
    }

    @Tool(
            name = "searchCodeBase",
            description = "Search for a keyword across java files and return matching java file names"
    )
    public List<String> searchCodeBase(String keyword){

        try(Stream<Path> paths = Files.walk(Paths.get(PROJECT_ROOT))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> containsKeyword(path, keyword))
                    .map(Path::toString)
                    .toList();
        } catch (IOException e) {
            return List.of("Error while searching code base: " + e.getMessage());
        }
    }

    private boolean containsKeyword(Path path, String keyword) {
        try {
            return Files.readAllLines(path).stream().anyMatch(line -> line.contains(keyword));
        } catch (IOException e) {
            return false;
        }
    }
}
