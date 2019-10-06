#!/usr/bin/env ruby

# sudo gem install yajl
require 'yajl'

f = ARGV[0]

o = nil

begin
    puts(f)
    json = File.read(f)
    parser = Yajl::Parser.new
    o = parser.parse( json )
    p o
    
    if o == nil
        exit 1
    else
        exit 0
    end
rescue JSON::ParserError => pe
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
end
