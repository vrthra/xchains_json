#!/usr/bin/env ruby

# sudo gem install oj
require 'oj'

f = ARGV[0]

o = nil

Oj.default_options = { :mode => :compat }

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
    puts("invalid2")
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

    #if er.to_s.include? "not a number or other value"
    #    puts("incomplete")
    #    exit 1
    #end


    if line.to_i == 2
        puts("incomplete")
        exit 1
    end
      
    puts("invalid3")
    exit 2
end
