Test assignment for AgileEngine

Program that analyzes HTML and finds a specific element, even after changes, using a set of extracted attributes.

To run the program use command from the directory where jar file is located or specify the path to the jar file:  

java -jar similar-element-locator.jar <input_origin_file_path> <input_other_sample_file_path> <element-id> 

Where:
    <platform> - the chosen language/platform;
    <program_path> - path to the executable app;
    <input_origin_file_path> - origin sample path to find the element with attribute id="make-everything-ok-button" and collect all the required information;
    <input_other_sample_file_path> - path to diff-case HTML file to search a similar element;
    
Example:
    java -jar similar-element-locator.jar ./../../examples/sample-0-origin.html ./../../examples/sample-1-evil-gemini.html make-everything-ok-button

In case of success the last line of output will contain path to the element that is chosen as the best match.



Results for sample pages:
1. html>body>div>div>div>div>div>div>a[2]
2. html>body>div>div>div>div>div>div>div>a[1]
3. html>body>div>div>div>div>div>div>a[1]
4. html>body>div>div>div>div>div>div>a[1]