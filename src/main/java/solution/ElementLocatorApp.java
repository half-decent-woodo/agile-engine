package solution;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.agileengine.JsoupFindByIdSnippet.findElementById;
import static com.agileengine.JsoupFindByIdSnippet.getAllElements;

public class ElementLocatorApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElementLocatorApp.class);

    public static void main(String[] args) {

        Path originalFilePath = Paths.get(args[0]);
        Path otherSampleFilePath = Paths.get(args[1]);
        String sourceElementId = getElementIdArg(args).orElse("make-everything-ok-button");

        Optional<Element> optionalElement = findElementById(originalFilePath.toFile(), sourceElementId);

        if (optionalElement.isEmpty()) {
            LOGGER.info("Element with id={} not found in original document", sourceElementId);
            return;
        }

        Element reference = optionalElement.get();
        List<Element> allElements = getAllElements(otherSampleFilePath.toFile());

        Optional<Element> optionalWinner = allElements.stream().max(Comparator.comparingInt(e -> similarityScore(e, reference)));

        if (optionalWinner.isEmpty()) {
            LOGGER.info("Winner not found. Is diff-case file empty?");
            return;
        }

        Element winner = optionalWinner.get();
        Deque<String> pathElements =  Stream.iterate(winner, Element::parent).limit(winner.parents().size() + 1)
                .map(e ->
                    e == winner ? e.tagName() + "["+(e.elementSiblingIndex() + 1) +"]" : e.tagName() // + 1 because xPath indexes are 1-based
                )
                .collect(Collectors.toCollection(ArrayDeque::new));

        String pathToWinner = StreamSupport.stream(Spliterators.spliteratorUnknownSize(pathElements.descendingIterator(), Spliterator.ORDERED), false)
                .collect(Collectors.joining(">"));

        System.out.println(pathToWinner);
    }

    private static int similarityScore(Element first, Element second) {
        Map<String, String> firstAttributes = attrsToMap(first.attributes());
        Map<String, String> secondAttributes = attrsToMap(second.attributes());

        return firstAttributes.entrySet().stream()
                .mapToInt(attribute -> {
                    String attrName = attribute.getKey();
                    String firstValue = attribute.getValue();

                    Optional<String> secondValue = Optional.ofNullable(secondAttributes.get(attrName));

                    return secondValue.map(val -> valuesSimilarity(val, firstValue))
                            .orElse(0);
                }).sum();
    }

    private static Integer valuesSimilarity(String firstVal, String secondVal) {
        if (firstVal.equals(secondVal)) {
            return 1;
        } else {
            return -1;
        }
    }


    private static Map<String, String> attrsToMap(Attributes attrs) {
        return attrs.asList().stream().collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
    }

    private static Optional<String> getElementIdArg(String[] args) {
        if (args.length > 2) {
            return Optional.of(args[2]);
        } else {
            return Optional.empty();
        }
    }
}
