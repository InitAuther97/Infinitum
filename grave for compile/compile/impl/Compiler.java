package org.dhwpcs.infinitum.chat.compile.impl;

import org.dhwpcs.infinitum.chat.compile.Compilable;
import org.dhwpcs.infinitum.chat.compile.CompilableSection;
import org.dhwpcs.infinitum.chat.compile.CompilationContext;
import org.dhwpcs.infinitum.chat.compile.CompilationResult;
import org.dhwpcs.infinitum.chat.compile.tokenize.StringTokenizer;
import org.dhwpcs.infinitum.chat.compile.tokenize.TokenizeSection;
import io.github.initauther97.nugget.util.RangeTree;

import java.util.*;
import java.util.stream.Collectors;

public class Compiler {

    public static final Compilable<String> PLAIN = new Compilable<>() {
        @Override
        public StringTokenizer tokenizer() {
            return raw -> Set.of(new TokenizeSection(raw, 0, raw.length()));
        }

        @Override
        public Map<String, ?> extractMeta(CompilableSection<String> result, CompilationContext ctx) {
            return Map.of("value",result);
        }

        @Override
        public CompilationResult<String> compile(Map<String, ?> meta, CompilationContext ctx) {
            return new PlainResult((String) meta.get("value"));
        }

        @Override
        public String key() {
            return "plain";
        }
    };
    public static final class ContextImpl implements CompilationContext {
        private final Set<Compilable<?>> entries = new HashSet<>();
        private final Set<Runnable> finisher = new HashSet<>();
        private final Map<String, Object> memories = new HashMap<>();
        private boolean root = false;

        private final ThreadLocal<List<?>> stackObject = ThreadLocal.withInitial(LinkedList::new);

        @Override
        public CompilationResult<?> compile(String command) {
            boolean isRoot = !root;
            if(!root) {
                root = true;
            } else if(memories.containsKey(command)) {
                CompilationResult<?> result = (CompilationResult<?>)getMemoryMirror(command);
                memories.remove(command);
                return result;
            }

            try {
                Set<CompilableSection<?>> sections = entries.stream()
                        .flatMap(cp -> cp.tokenizer().tokenize(command).stream()
                                .map(result -> result.withCompilable(cp)))
                        .collect(Collectors.toUnmodifiableSet());
                RangeTree<CompilableSection<?>> tree = RangeTree.create(command.length(), sections);
                tree.forEach(section -> );
                return
            } finally {
                finisher.forEach(Runnable::run);
                finisher.clear();
                if(isRoot) {

                }
            }
        }

        @Override
        public Set<Compilable<?>> getEntries() {
            return entries;
        }

        @Override
        public boolean register(Compilable<?> compilable) {
            return entries.add(compilable);
        }

        @Override
        public void addFinishAction(Runnable r) {

        }

        @Override
        public String setMemoryMirror(Object obj) {
            UUID uid = UUID.randomUUID();
            memories.put(uid.toString(), obj);
            return uid.toString();
        }

        @Override
        public Object getMemoryMirror(String key) {
            return memories.get(key);
        }
    }


}
