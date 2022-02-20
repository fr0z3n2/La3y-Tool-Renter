package com.la3ypotato.toolrenter.tool;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Tools {
    private final int EXPECTED_TOOL_ARGS = 7;
    private final String FILE_DELIMITER= ",";
    private String resourceCSVFile = "/tools.csv";
    private Map<String, Tool> availableTools;

    public Tools() {
        availableTools = new HashMap<>();
    }

    // Loads the tools from the packaged CSV file.
    private void loadTools() {
        try {
            InputStream in = getClass().getResourceAsStream(resourceCSVFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = "";
            // Read each line of the CSV resource. Each line can be a created tool.
            while ( (line = br.readLine()) != null ) {
                String[] toolInfo = line.split(FILE_DELIMITER);
                Tool createdTool = createTool(toolInfo);
                availableTools.put(createdTool.toolCode, createdTool);
            }
            // Closing the BufferedReader.
            br.close();
        // createTool can throw an IllegalArgumentException if an incorrect
        // number of fields are in the CSV file.
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private Tool createTool(String ...toolInfo) throws IllegalArgumentException {
        if (toolInfo.length != EXPECTED_TOOL_ARGS) {
            throw new IllegalArgumentException("Incorrect number of tool parameters!\n" +
                                               "Expected: " + EXPECTED_TOOL_ARGS + "\n" +
                                               "Received: " + toolInfo.length + "\n" +
                                               "Problematic tool line: " + Arrays.toString(toolInfo));
        }
        // Pull apart the toolInfo array.
        String toolCode = toolInfo[0];
        String toolType = toolInfo[1];
        String brand = toolInfo[2];
        String dailyCharge = toolInfo[3];
        String weekdayCharge = toolInfo[4];
        String weekendCharge = toolInfo[5];
        String holidayCharge = toolInfo[6];
        // Defining the tool.
        Tool tool = new Tool(toolCode, toolType, brand);
        // Convert the read in string to a double.
        tool.dailyCharge = Double.valueOf(dailyCharge);
        // Convert the 'yes'/'no' strings to a primitive boolean.
        tool.weekdayCharge = weekdayCharge.toLowerCase().equals("yes") ? true : false;
        tool.weekendCharge = weekendCharge.toLowerCase().equals("yes") ? true : false;
        tool.holidayCharge = holidayCharge.toLowerCase().equals("yes") ? true : false;
        return tool;
    }

    public Map<String, Tool> getAvailableTools() {
        loadTools();
        return availableTools;
    }
}
