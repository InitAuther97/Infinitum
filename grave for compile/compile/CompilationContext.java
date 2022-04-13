package org.dhwpcs.infinitum.chat.compile;

import java.util.Set;

public interface CompilationContext {

    CompilationResult<?> compile(String command);

    Set<Compilable<?>> getEntries();

    boolean register(Compilable<?> compilable);

    void addFinishAction(Runnable r);

    String setMemoryMirror(Object obj);

    Object getMemoryMirror(String key);
}
