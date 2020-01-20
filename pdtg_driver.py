#!/usr/bin/env python3
# coding: utf-8
# import pudb
# bp = pudb.set_trace

import json
import urllib.parse as uparse
import string
import random
import subprocess
import os
import copy


COMPLETE = 0
INCOMPLETE = 1
WRONG = 2
TIMEOUT = 10
NO_PRINTABLE_STRINGS = 100
NO_TRIES_TILL_ABORT = 250

BASE_DIR = os.path.dirname(os.path.realpath(__file__))
PARSERS_DIR = os.path.join(BASE_DIR, "parsers")

parsers = {
    "java_actson_1_2_0":
       {
           "command": ["/usr/bin/java", "-jar", os.path.join(PARSERS_DIR, "java_actson_1_2_0/TestJSONParsing.jar")],
            "logfile": "java_actson_1_2_0"
       },
    "java_bfo":
       {
           "command": ["/usr/bin/java", "-jar", os.path.join(PARSERS_DIR, "java_bfo/TestJSONParsing.jar")],
            "logfile": "java_bfo"
       },
    "java_com_leastfixedpoint_json_1_0":
       {
           "command": ["/usr/bin/java", "-jar", os.path.join(PARSERS_DIR, "java_com_leastfixedpoint_json_1_0/TestJSONParsing.jar")],
            "logfile": "java_com_leastfixedpoint_json_1_0"
       },
    "java_gson_2_8_6":
       {
           "command": ["/usr/bin/java", "-jar", os.path.join(PARSERS_DIR, "java_gson_2_8_6/TestJSONParsing.jar")],
           "logfile": "java_gson_2_8_6"
       },
    "java_jackson_2_8_4":
       {
           "command": ["/usr/bin/java", "-jar", os.path.join(PARSERS_DIR, "java_jackson_2_8_4/TestJSONParsing.jar")],
           "logfile": "java_jackson_2_8_4"
       },
    "java_mergebase_json_2019_09_09":
       {
           "command": ["/usr/bin/java", "-jar", os.path.join(PARSERS_DIR, "java_mergebase_json_2019_09_09/TestJSONParsing.jar")],
           "logfile": "java_mergebase_json_2019_09_09"
       },
    "java_nanojson_1_1":
       {
           "command": ["/usr/bin/java", "-jar", os.path.join(PARSERS_DIR, "java_nanojson_1_1/TestJSONParsing.jar")],
           "logfile": "java_nanojson_1_1"
       },
    "java_org_json_2016_08":
       {
           "command": ["/usr/bin/java", "-jar", os.path.join(PARSERS_DIR, "java_org_json_2016_08/TestJSONParsing.jar")],
           "logfile": "java_org_json_2016_08"
       },
    "java_simple_json_1_1_1":
       {
           "command": ["/usr/bin/java", "-jar", os.path.join(PARSERS_DIR, "java_simple_json_1_1_1/TestJSONParsing.jar")],
           "logfile": "java_simple_json_1_1_1"
       },
    "java_stringtree":
       {
           "command": ["/usr/bin/java", "-jar", os.path.join(PARSERS_DIR, "java_stringtree/TestJSONParsing.jar")],
           "logfile": "java_stringtree"
       },
    "javascript_json":
       {
           "command": ["node", os.path.join(PARSERS_DIR, "javascript_json/test_json.js")],
           "logfile": "javascript_json"
       },
    "ruby_plain_json":
       {
           "command": ["ruby", os.path.join(PARSERS_DIR, "ruby_plain_json/test_json.rb")],
           "logfile": "ruby_plain_json"
       },
    "ruby_json_re":
        {
            "command": ["ruby", os.path.join(PARSERS_DIR, "ruby_json_re/test_json_re.rb")],
            "logfile": "ruby_json_re"
        },
    "ruby_oj_compat":
        {
            "command": ["ruby", os.path.join(PARSERS_DIR, "ruby_oj_compat/test_oj_compat.rb")],
            "logfile": "ruby_oj_compat"
        },
    "ruby_oj_strict":
        {
            "command": ["ruby", os.path.join(PARSERS_DIR, "ruby_oj_strict/test_oj_strict.rb")],
            "logfile": "ruby_oj_strict"
        },
    "python_json_3_7_4":
       {
           "command": os.path.join(PARSERS_DIR, "python_json_3_7_4/TestJSONParsing.py"),
           "logfile": "python_json_3_7_4"
       }
}

class O:
    def __init__(self, **keys): self.__dict__.update(keys)
    def __repr__(self): return str(self.__dict__)

def init_log(prefix, var, module):
    with open('%s.log' % module, 'a+') as f:
        print(prefix, ':==============',var, file=f)

def do(com, env=None, shell=False, log=False, **args):
    command = copy.deepcopy(com)
    if isinstance(command[0], list):
        command_tmp = command[0]
        command_tmp.append(command[1])
        command = command_tmp
    #print(command)
    result = subprocess.Popen(command,
        stdout = subprocess.PIPE,
        stderr = subprocess.STDOUT,
    )
    stdout, stderr = result.communicate(timeout=TIMEOUT)
    if log:
        with open('do.log', 'a+') as f:
            print(json.dumps({'cmd':command, 'env':env, 'exitcode':result.returncode}), env, file=f)
    return O(returncode=result.returncode, stdout=stdout, stderr=stderr)

def get_next_char(log_level):
    set_of_chars = string.printable # ['[',']','{','}','(',')','<','>','1','0','a','b',':','"',',','.', '\'']
    idx = random.randrange (0,len(set_of_chars),1)
    input_char = set_of_chars[idx]
    return input_char

def get_next_char_v2(log_level, no_char):
    set_of_chars = string.printable # ['[',']','{','}','(',')','<','>','1','0','a','b',':','"',',','.', '\'']
    testset = [item for item in set_of_chars if item not in no_char]
    #print("length:",len(testset))
    idx = random.randrange (0,len(testset),1)
    input_char = testset[idx]
    return input_char

def validate(cmd, fname):
    #print([cmd, fname])
    res = do([cmd, fname])
    #print("return value:", res.stdout.decode('utf-8'))
    txt = res.stdout.decode('utf-8')
    if 'Error: LinkageError' in txt:
        print('Linkage error', txt)
        sys.exit(1)
    print("return value:", res.stdout)
    return res.returncode

def generate(cmd, log_level):
    """
    Feed it one character at a time, and see if the parser rejects it. 
    If it does not, then append one more character and continue. 
    If it rejects, replace with another character in the set. 
    :returns completed string
    """
    prev_str = ""
    no_char = set()
    while True:
        if len(no_char) == NO_PRINTABLE_STRINGS: # max number of printable chars, if reached -> all chars tested
            return None
        if len(prev_str) > NO_TRIES_TILL_ABORT: # max number of chars in test string
            return None
        #char = get_next_char(log_level, no_char)
        char = get_next_char_v2(log_level, no_char)
        #print("char:", str(char))
        curr_str = prev_str + str(char)
        #print("curr_str:", curr_str)
        print('\t', repr(curr_str))
        with open('cur_str.json_', 'w+') as f:
            f.write(curr_str)
        rv = validate(cmd, 'cur_str.json_')
        if rv == COMPLETE:
            return curr_str
        elif rv == INCOMPLETE:
            prev_str = curr_str
            no_char = set()
            continue
        elif rv == WRONG: # try again with a new random character do not save current character
            no_char.add(char)
            continue
        else:
            print("ERROR What is this I dont know !!!")
            break
    return None

def create_valid_strings(current_parser, n, log_level):
    i = 0
    while True:
        created_string = generate(parsers[current_parser]['command'], log_level)
        if created_string is not None:
            print(repr(created_string))
            with open('%s.log' % parsers[current_parser]["logfile"], 'a+') as f:
                print(uparse.quote(created_string), file=f)
            i = i +1
            if (i >= n):
                break

def main(cmd):
    create_valid_strings(cmd, 100000, 0)

import sys
if __name__ == '__main__':
    #main(sys.argv[1])
    #main('python_json_3_7_4') # works
    main('java_actson_1_2_0') # only returns valid & invalid (throws no exceptions)
    #main('java_bfo') # works
    #main('java_com_leastfixedpoint_json_1_0') # endless loop under certain conditions
    #main('java_gson_2_8_6') # works
    #main('java_jackson_2_8_4') # seems to work :)
    #main('java_mergebase_json_2019_09_09') # somehow incorrect due to error "Never found", it does not work when tagging this either as "invalid" or "incomplete"
    #main('java_nanojson_1_1') # works
    #main('java_org_json_2016_08') # test with "incomplete.json" fails (declared as "invalid") not distinguishable with "invalid.json", maybe error of the parser
    #main('java_simple_json_1_1_1') # works
    #main('java_stringtree') # work in progress
    #main('javascript_json') # works
    #main('ruby_plain_json') # test with "incomplete.json" fails (declared as "invalid") not distinguishable with "invalid.json", maybe error of the parser
    #main('ruby_json_re') # only returns valid & invalid (throws no exceptions)
    #main('ruby_oj_compat') # no distinct error messages, "invalid" is not distinguishable from "incomplete"
    #main('ruby_oj_strict') # no distinct error messages, "invalid" is not distinguishable from "incomplete"
