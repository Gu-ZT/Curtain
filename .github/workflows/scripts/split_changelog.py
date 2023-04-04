import sys

if __name__ == '__main__':
    args = sys.argv
    if len(args) == 1:
        file_name = 'CHANGELOG.MD'
    else:
        file_name = args[1]

    with open(file_name, 'r', encoding='utf-8') as f:
        lines = f.readlines()

    new_log = ''
    flag = False
    for line in lines:
        if flag:
            if line.startswith('##'):
                break
            new_log += line
            ...
        else:
            if line.startswith('##'):
                flag = True
                new_log += line

    with open('NEW_LOG.MD', 'w', encoding='utf-8') as f:
        f.write(new_log)