package com.siiam.compiler.parser.ast;

import com.siiam.compiler.exception.InterpreterException;
import com.siiam.compiler.scope.Scope;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@RequiredArgsConstructor
public class FieldExpr implements Expr{
    private final Expr expr;
    private final String name;
    private boolean optional = false;

    @Override
    public Object eval(Scope scope) {
        var value = expr.eval(scope);

        if(value == null){
            if(optional){
                return null;
            }

            throw new InterpreterException("Null pointer exception");
        }

        if( value instanceof Map ){
            var map = (Map<String, Object>) value;
            return map.get(name);
        }

        var valClass = value.getClass();
        try {
            var field = valClass.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new InterpreterException("Expected map or valid object in field expr!", e);
        }
    }

    @Override
    public Object assign(Scope scope, Object obj) {
        var value = expr.eval(scope);

        if(value == null){
            if(optional){
                return null;
            }

            throw new InterpreterException("Null pointer exception");
        }

        if( value instanceof Map ){
            var map = (Map<String, Object>) value;
            return map.put(name, obj);
        }

        var valClass = value.getClass();
        try {
            var field = valClass.getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
            return value;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new InterpreterException("Expected map or valid object in field expr!", e);
        }
    }
}