#!/usr/bin/ruby

require 'json'

f = ARGV[0]

o = nil

begin
    puts(f)
    json = File.read(f)
    o = JSON.parse(json, {:quirks_mode => true})
    p o
    puts(o)
    if o == nil
        puts("incomplete")
        exit 1
    else
        puts("valid")
        exit 0
    end
rescue JSON::ParserError => e
    puts(e)
    puts("invalid")
    exit 2
end
