package com.yanan.util.xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class SaxReader {
    private final XmlPullParser xmlPullParser;

    public SaxReader(XmlPullParser xmlPullParser){
        this.xmlPullParser = xmlPullParser;
    }
    public Node read() throws XmlPullParserException, IOException {
        Node rootNode = new XmlNode();
        Stack<String> stack = new Stack<>();
        parse(rootNode,stack);
        return rootNode;
    }
    public void parse(Node node, Stack<String> stack) throws XmlPullParserException, IOException {

        int eventType = xmlPullParser.getEventType();
        int depth = xmlPullParser.getDepth();
        boolean isChild = true;
        boolean isRead = false;
        System.err.println("阅读深度:"+depth+",节点:"+node+"，栈:"+stack);
        String tagName;
        Object currentInstance = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    stack.push(tagName);
                    String path = getPath(stack);
                    System.err.println("当前深度:"+xmlPullParser.getDepth()+"==>"+tagName);
                    if(xmlPullParser.getDepth() > depth+1){
                        parse(node,stack);
                    }else{
//                       node.setName(tagName);
                        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
//                            node.
                            System.err.println("属性:" + xmlPullParser.getAttributeName(i) + "=>" + xmlPullParser.getAttributeValue(i));
                        }
                    }
                    System.err.println("start:" + path + "==>" + xmlPullParser.getText() + "==>" + xmlPullParser.getAttributeCount() + "-->" + stack);
                    break;
                case XmlPullParser.END_DOCUMENT:
                    System.err.println("end doc:" + xmlPullParser.getName());
                    break;
                case XmlPullParser.END_TAG:
                    stack.pop();
                    System.err.println("end:" + xmlPullParser.getName());
//                    if(node == null)
//                        this.node.add(currentInstance);
//                    else
                        return;
                case XmlPullParser.TEXT:
                    System.err.println("text:" + xmlPullParser.getName() + "==>" + xmlPullParser.getText() + "--->" + stack);
                    break;
                default:
                    break;

            }
            //调用parser.next()方法解析下一个元素
            eventType = xmlPullParser.next();
        }
    }
    private String getPath(Stack<String> stack) {
        StringBuilder stringBuilder =  new StringBuilder();
        for(int i = 0;i<stack.size() ; i++){
            stringBuilder.append(stack.get(i));
            if(i < stack.size() - 1)
                stringBuilder.append("/");
        }
        return stringBuilder.toString();
    }
}
