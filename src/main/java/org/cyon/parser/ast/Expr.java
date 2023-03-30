package org.cyon.parser.ast;

import org.cyon.visitor.*;

public interface Expr {
    void visit(Visitor visitor);
    <T> T visit(ResultVisitor<T> visitor);
    default Expr expand(){
        var expander = new Expander();
        return expander.expand(this);
    }
    default Expr reduce(){
        var reducer = new Reducer();
        return reducer.reduce(this);
    };
    default <T> T deserialize(Class<T> clazz){
        var deserializer = new Deserializer();
        return deserializer.deserialize(this, clazz);
    }
    default String stringify(boolean pretty){
        Stringifier stringifier;
        if(pretty){
            stringifier = new PrettyStringifier();
        }else{
            stringifier = new CompactStringifier();
        }
        return stringifier.stringify(this);
    }
}