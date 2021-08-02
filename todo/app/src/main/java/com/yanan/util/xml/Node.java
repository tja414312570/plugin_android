package com.yanan.util.xml;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

public interface Node extends Cloneable {
//    // W3C DOM complient node type codes
//
//    /** Matches Element nodes */
//    short ANY_NODE = 0;
//
//    /** Matches Element nodes */
//    short ELEMENT_NODE = 1;
//
//    /** Matches elements nodes */
//    short ATTRIBUTE_NODE = 2;
//
//    /** Matches elements nodes */
//    short TEXT_NODE = 3;
//
//    /** Matches elements nodes */
//    short CDATA_SECTION_NODE = 4;
//
//    /** Matches elements nodes */
//    short ENTITY_REFERENCE_NODE = 5;
//
//    /** Matches elements nodes */
//
//    // public static final short ENTITY_NODE = 6;
//    /** Matches ProcessingInstruction */
//    short PROCESSING_INSTRUCTION_NODE = 7;
//
//    /** Matches Comments nodes */
//    short COMMENT_NODE = 8;
//
//    /** Matches Document nodes */
//    short DOCUMENT_NODE = 9;
//
//    /** Matches DocumentType nodes */
//    short DOCUMENT_TYPE_NODE = 10;
//
//    // public static final short DOCUMENT_FRAGMENT_NODE = 11;
//    // public static final short NOTATION_NODE = 12;
//
//    /** Matchs a Namespace Node - NOTE this differs from DOM */
//
//    // XXXX: ????
//    short NAMESPACE_NODE = 13;
//
//    /** Does not match any valid node */
//    short UNKNOWN_NODE = 14;
//
//    /** The maximum number of node types for sizing purposes */
//    short MAX_NODE_TYPE = 14;
//
//    boolean supportsParent();
//
//
//    Element getParent();
//
//
//    void setParent(Element parent);
//
//    boolean isReadOnly();
//
//
//    boolean hasContent();
//
//    String getName();
//
//
//    void setName(String name);
//
//
//    String getText();
//
//    void setText(String text);
//
//    String getStringValue();
//
//    String getPath();
//
//    String getPath(Element context);
//
//    String getUniquePath();
//
//    String getUniquePath(Element context);
//
//    String asXML();
//
//    void write(Writer writer) throws IOException;
//
//    short getNodeType();
//
//    String getNodeTypeName();
//
//    Node detach();
//
//    List selectNodes(String xpathExpression);
//
//    Object selectObject(String xpathExpression);
//
//    List selectNodes(String xpathExpression, String comparisonXPathExpression);
//
//    List selectNodes(String xpathExpression, String comparisonXPathExpression,
//                     boolean removeDuplicates);
//
//    Node selectSingleNode(String xpathExpression);
//
//    String valueOf(String xpathExpression);
//
//    Number numberValueOf(String xpathExpression);
//
//    boolean matches(String xpathExpression);
//
//    Node asXPathResult(Element parent);
//
//    Object clone();
//
//    /**
//     * Adds a new <code>Text</code> node with the given text to this element.
//     *
//     * @param text
//     *            is the text for the <code>Text</code> node.
//     *
//     * @return this <code>Element</code> instance.
//     */
//    Element addText(String text);
//
//    // Typesafe modifying methods
//    // -------------------------------------------------------------------------
//
//    /**
//     * Adds the given <code>Attribute</code> to this element. If the given
//     * node already has a parent defined then an
//     * <code>IllegalAddException</code> will be thrown. Attributes with null
//     * values are silently ignored.
//     *
//     * <p>
//     * If the value of the attribute is null then this method call will remove
//     * any attributes with the QName of this attribute.
//     * </p>
//     *
//     * @param attribute
//     *            is the attribute to be added
//     */
//    void add(Attribute attribute);
//
//    /**
//     * Adds the given <code>CDATA</code> to this element. If the given node
//     * already has a parent defined then an <code>IllegalAddException</code>
//     * will be thrown.
//     *
//     * @param cdata
//     *            is the CDATA to be added
//     */
//    void add(CDATA cdata);
//
//    /**
//     * Adds the given <code>Entity</code> to this element. If the given node
//     * already has a parent defined then an <code>IllegalAddException</code>
//     * will be thrown.
//     *
//     * @param entity
//     *            is the entity to be added
//     */
//    void add(Entity entity);
//
//    /**
//     * Adds the given <code>Text</code> to this element. If the given node
//     * already has a parent defined then an <code>IllegalAddException</code>
//     * will be thrown.
//     *
//     * @param text
//     *            is the text to be added
//     */
//    void add(Text text);
//
//    /**
//     * Adds the given <code>Namespace</code> to this element. If the given
//     * node already has a parent defined then an
//     * <code>IllegalAddException</code> will be thrown.
//     *
//     * @param namespace
//     *            is the namespace to be added
//     */
//    void add(Namespace namespace);
//
//    /**
//     * Removes the given <code>Attribute</code> from this element.
//     *
//     * @param attribute
//     *            is the attribute to be removed
//     *
//     * @return true if the attribute was removed
//     */
//    boolean remove(Attribute attribute);
//
//    /**
//     * Removes the given <code>CDATA</code> if the node is an immediate child
//     * of this element. If the given node is not an immediate child of this
//     * element then the {@link Node#detach()}method should be used instead.
//     *
//     * @param cdata
//     *            is the CDATA to be removed
//     *
//     * @return true if the cdata was removed
//     */
//    boolean remove(CDATA cdata);
//
//    /**
//     * Removes the given <code>Entity</code> if the node is an immediate child
//     * of this element. If the given node is not an immediate child of this
//     * element then the {@link Node#detach()}method should be used instead.
//     *
//     * @param entity
//     *            is the entity to be removed
//     *
//     * @return true if the entity was removed
//     */
//    boolean remove(Entity entity);
//
//    /**
//     * Removes the given <code>Namespace</code> if the node is an immediate
//     * child of this element. If the given node is not an immediate child of
//     * this element then the {@link Node#detach()}method should be used
//     * instead.
//     *
//     * @param namespace
//     *            is the namespace to be removed
//     *
//     * @return true if the namespace was removed
//     */
//    boolean remove(Namespace namespace);
//
//    /**
//     * Removes the given <code>Text</code> if the node is an immediate child
//     * of this element. If the given node is not an immediate child of this
//     * element then the {@link Node#detach()}method should be used instead.
//     *
//     * @param text
//     *            is the text to be removed
//     *
//     * @return true if the text was removed
//     */
//    boolean remove(Text text);
//
//    // Text methods
//    // -------------------------------------------------------------------------
//    String getText();
//
//    String getTextTrim();
//
//    String getStringValue();
//
//    Object getData();
//    void setData(Object data);
//
//    // Attribute methods
//    // -------------------------------------------------------------------------
//
//    List attributes();
//
//    void setAttributes(List attributes);
//
//    int attributeCount();
//
//    Iterator attributeIterator();
//
//    Attribute attribute(int index);
//
//    Attribute attribute(String name);
//
//    String attributeValue(String name);
//
//    String attributeValue(String name, String defaultValue);
//
//    void setAttributeValue(String name, String value);
//
//
//    // Content methods
//    // -------------------------------------------------------------------------
//    Element element(String name);
//
//    List elements();
//
//    List elements(String name);
//
//    List elements(QName qName);
//
//    Iterator elementIterator();
//
//    Iterator elementIterator(String name);
//
//
//    // Helper methods
//
//    boolean isRootElement();
//
//    boolean hasMixedContent();
//
//    boolean isTextOnly();
//
//    void appendAttributes(Element element);
//
//    Element createCopy();
//
//    Element createCopy(String name);
//
//    String elementText(String name);
//
//    String elementTextTrim(String name);
//
//    Node getXPathResult(int index);
//}
}
