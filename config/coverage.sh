#!/bin/bash

# Default path if none provided
REPORT_PATH=${1:-build/reports/jacoco/test/jacocoTestReport.xml}

if [ ! -f "$REPORT_PATH" ]; then
    echo "Error: Report not found at $REPORT_PATH"
    exit 1
fi

perl -0777 -ne '
    while(/<counter type="(INSTRUCTION|BRANCH|COMPLEXITY|LINE|METHOD|CLASS)" missed="(\d+)" covered="(\d+)"\/>/g) {
        $m{$1}=$2; $c{$1}=$3;
    }
    END {
        foreach $t ("Instruction","Branch","Complexity","Line","Method","Class") {
            $key = uc($t);
            if(exists $m{$key}) {
                $tot = $m{$key} + $c{$key};
                printf("%s Coverage: %.2f%%\n", $t, ($c{$key}/$tot)*100) if $tot > 0;
            }
        }
    }
' "$REPORT_PATH"
