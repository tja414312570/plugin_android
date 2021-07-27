package com.yanan.framework;


import com.yanan.util.CacheHashMap;
import com.yanan.util.HashMaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StringHolder {
	private static final Map<String,StringHolderProvider> holderMap = new HashMaps<>();
	private static final CacheHashMap<String,List<Token>> tokenCache = new CacheHashMap<>();
	public static void register(String resource,StringHolderProvider stringHolderProvider) {
		holderMap.put(resource,stringHolderProvider);
	}
    public static class Token {
    	public static final int STRING = 0;
    	public static final int EXPRESS = 1;
        final private String token;
		final private String name;
		final private String attr;
		private final String args;
		private String value;
		final private int type;

		@Override
		public String toString() {
			return "Token{" +
					"token='" + token + '\'' +
					", name='" + name + '\'' +
					", attr='" + attr + '\'' +
					", args='" + args + '\'' +
					", value='" + value + '\'' +
					", type=" + type +
					'}';
		}

		public Token(String token, String name, String attr,String args,  int type) {
			super();
			this.token = process(token);
			this.name = process(name);
			this.attr = process(attr);
			this.args = process(args);
			this.type = type;
		}
		public String getArgs() {
			return args;
		}
        public String getToken() {
            return token;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

		public String getAttr() {
			return attr;
		}

		public void setValue(String value) {
            this.value = value;
        }

        public int getType() {
            return type;
        }

    }
    private static String process(String args){
		if(args == null)
			return null;
		return args.replace("\\{","{")
				.replace("\\}","}")
				.replace("\\@","@")
				.replace("\\:",":");
	}
    public static List<Token> getTokenList(String express){
		List<Token> tokenList = tokenCache.get(express);
		if(tokenList == null ){
			tokenList = decodeTokenList(express);
			tokenCache.puts(express,tokenList);
		}
		List<Token> tokenListClone = new ArrayList<>();
		for(Token token : tokenList){
			tokenListClone.add(new Token(token.getToken(),token.getName(),token.getAttr(),token.getArgs(),token.getType()));
		}
		return tokenListClone;
	}
	public static List<Token> decodeToken(String express){
		List<Token> tokenList = getTokenList(express);
		for(Token token : tokenList){
			if(token.getType() == Token.EXPRESS){
				String value = null;
				if(token.getAttr()!=null){
					StringHolderProvider stringHolderProvider = holderMap.get(token.getAttr());
					if(stringHolderProvider != null)
						value = stringHolderProvider.getValue(token.getName(),token.getAttr(),token.getArgs(),token.getToken());
				}else{
					for (StringHolderProvider stringHolderProvider : holderMap.values()) {
						if ((value = stringHolderProvider.getValue(token.getName(), token.getAttr(),token.getArgs(), token.getToken())) != null)
							break;
					}
				}
				token.setValue(value);
			}
		}
		return tokenList;
	}
    public static String decodeString(String express){
		List<Token> tokenList = getTokenList(express);
		StringBuilder result = new StringBuilder();
		for(Token token : tokenList){
			if(token.getType() == Token.EXPRESS){
				String value = null;
				if(token.getAttr()!=null){
					StringHolderProvider stringHolderProvider = holderMap.get(token.getAttr());
					if(stringHolderProvider != null)
						value = stringHolderProvider.getValue(token.getName(),token.getAttr(),token.getArgs(),token.getToken());
				}else{
					for (StringHolderProvider stringHolderProvider : holderMap.values()) {
						if ((value = stringHolderProvider.getValue(token.getName(), token.getAttr(),token.getArgs(), token.getToken())) != null)
							break;
					}
				}
				result.append(value);
			}else{
				result.append(token.getToken());
			}
		}
        return result.toString();
    }

    private static List<Token> decodeTokenList(String express) {
    	List<Token> tokenList = new ArrayList<>();
    	int start = 0;
		int index = -1;
		while((index = express.indexOf("{",index+1)) != -1  ) {
			if(express.charAt(index-1) == '\\') 
				continue;
			if(index > start)
				tokenList.add(new Token(express.substring(start,index),null,null,null,Token.STRING));
			int endex = index;
			while((endex = express.indexOf("}",endex))!=-1){
				if(express.charAt(endex-1) == '\\') 
					continue;
				break;
			}
			if(endex == -1)
				throw new IllegalArgumentException("express end of '}' ");
			String token = express.substring(index,endex+1);
			String name = token.substring(1,token.length()-1);
			String attr = null;
			String args = null;
			int foundAt = -1;
			for(int i = 1;i < token.length()-1;i++) {
				if(token.charAt(i) == '\\') {
					i++;
					continue;
				}
				if(token.charAt(i)=='@' && foundAt == -1) {
					attr = token.substring(i+1);
					name = token.substring(0,i);
					foundAt = i;
				}
				if(token.charAt(i)==':' && foundAt != -1) {
					attr = token.substring(foundAt+1,i);
					args = token.substring(i+1);
				}

			}
			tokenList.add(new Token(token,name,attr,args,Token.EXPRESS));
			start = endex+1;
    	}
		if(start<express.length())
			tokenList.add(new Token(express.substring(start),null,null,null,Token.STRING));
		return tokenList;
	}

	public static void main(String[] args) {
		System.err.println(decodeString("select {column@Resource} from {table} where"));
//		System.err.println(decodeString("select"));
//		System.err.println(decodeString("select {column\\@Resource} from {table} where"));
//		System.err.println(decodeString("select {column\\@Resource} from {table}"));
    }
}
