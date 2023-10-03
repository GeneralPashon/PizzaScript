package com.pizzascript;

import java.io.File;

public class NasmUtils{

    private static final File output = new File("output/");

    public static void compile(String name){
        try{
            new ProcessBuilder()
                .directory(output)
                .command("nasm", "-felf64", name + ".asm")
                .inheritIO()
                .start()
                .waitFor();


            new ProcessBuilder()
                .directory(output)
                .command("ld", name + ".o", "--output", name + ".out", "-e", "main")
                .inheritIO()
                .start()
                .waitFor();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void run(String name){
        try{
            new ProcessBuilder()
                .command("output/" + name + ".out")
                .inheritIO()
                .start();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void compileAndRun(String name){
        // nasm -felf64 main.asm && ld main.o --output main.out && ./main.out
        compile(name);
        run(name);
    }

    public static String getNasmHelloWorld(){
        return
        """
                            global    main
                            section   .text
                main:
                            mov       rax, 1                  ; system call for write
                            mov       rdi, 1                  ; file handle 1 is stdout
                            mov       rsi, message            ; address of string to output
                            mov       rdx, 14                 ; number of bytes
                            syscall                           ; invoke operating system to do the write
                
                            mov       rax, 60                 ; system call for exit
                            xor       rdi, rdi                ; exit code 0
                            syscall                           ; invoke operating system to exit
                
                            section   .data
                message:    db        "Hello, World!", 0x0a   ; note the newline at the end
                
        """;
    }

}
