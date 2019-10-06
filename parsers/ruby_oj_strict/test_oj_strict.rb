#!/usr/bin/env ruby

# sudo gem install oj
require 'oj'

f = ARGV[0]

o = nil

Oj.default_options = { :mode => :strict }

begin
    puts(f)
    json = File.read(f)

    len = json.length
    puts("len: " + len.to_s)

    o = Oj.load( json )
    p o
    
    if o == nil
        puts("invalid1")
        exit 1
    else
        puts("valid")
        exit 0
    end
rescue Oj::ParseError => pe
    puts(pe)
    if pe.to_s.include? "expected true" or pe.to_s.include? "not terminated" or pe.to_s.include? "expected false"
        puts("incomplete")
        exit 1
    end

    # you can only dinstinguish between start of "incomplete" and a wrong next char, when the wrong is already written (in this case the 'K')
    
    #'t'
    #return value: b'cur_str.json_\nlen: 1\nexpected true () at line 1, column 2 [parse.c:121]\nincomplete\n'
    #'tK'
    #return value: b'cur_str.json_\nlen: 2\nexpected true () at line 1, column 2 [parse.c:121]\nincomplete\n'
    #'tKc'
    #return value: b'cur_str.json_\nlen: 3\nexpected true () at line 1, column 2 [parse.c:121]\nincomplete\n'
    #'tKcx'
    #return value: b'cur_str.json_\nlen: 4\nexpected true () at line 1, column 2 [parse.c:121]\nincomplete\n'

    puts("invalid")
    exit 2
rescue EncodingError => er
    puts(er)
    error_parts = er.to_s.split(" ")
    
    line = 0
    column = 0
    error_parts.each_with_index do |item, index|
        if item == "line"
            line = error_parts[index + 1][0...-1]
            puts("line: " + line)
        end
        if item == "column"
            column = error_parts[index + 1]
            puts("column: " + column)
        end
    end

    if line.to_i == 2
        puts("incomplete")
        exit 1
    end
      
    puts("invalid3")
    exit 2
end
