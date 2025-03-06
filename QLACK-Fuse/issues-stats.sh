#!/usr/bin/env bash

gh auth login

REPO=QLACK/QLACK-Java
LIMIT=1000

OPEN=$(gh issue list --repo $REPO --state open --limit $LIMIT --json number | jq '. | length')
CLOSED=$(gh issue list --repo $REPO --state closed --limit $LIMIT --json number | jq '. | length')

BUG_LOW_OPEN=$(gh issue list --repo $REPO --state open --label "T/bug" --label "P/low" --limit $LIMIT --json number | jq '. | length')
BUG_LOW_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/bug" --label "P/low" --limit $LIMIT --json number | jq '. | length')
BUG_MEDIUM_OPEN=$(gh issue list --repo $REPO --state open --label "T/bug" --label "P/medium" --limit $LIMIT --json number | jq '. | length')
BUG_MEDIUM_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/bug" --label "P/medium" --limit $LIMIT --json number | jq '. | length')
BUG_HIGH_OPEN=$(gh issue list --repo $REPO --state open --label "T/bug" --label "P/high" --limit $LIMIT --json number | jq '. | length')
BUG_HIGH_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/bug" --label "P/high" --limit $LIMIT --json number | jq '. | length')
BUG_CRITICAL_OPEN=$(gh issue list --repo $REPO --state open --label "T/bug" --label "P/critical" --limit $LIMIT --json number | jq '. | length')
BUG_CRITICAL_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/bug" --label "P/critical" --limit $LIMIT --json number | jq '. | length')

DOCUMENTATION_LOW_OPEN=$(gh issue list --repo $REPO --state open --label "T/documentation" --label "P/low" --limit $LIMIT --json number | jq '. | length')
DOCUMENTATION_LOW_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/documentation" --label "P/low" --limit $LIMIT --json number | jq '. | length')
DOCUMENTATION_MEDIUM_OPEN=$(gh issue list --repo $REPO --state open --label "T/documentation" --label "P/medium" --limit $LIMIT --json number | jq '. | length')
DOCUMENTATION_MEDIUM_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/documentation" --label "P/medium" --limit $LIMIT --json number | jq '. | length')
DOCUMENTATION_HIGH_OPEN=$(gh issue list --repo $REPO --state open --label "T/documentation" --label "P/high" --limit $LIMIT --json number | jq '. | length')
DOCUMENTATION_HIGH_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/documentation" --label "P/high" --limit $LIMIT --json number | jq '. | length')
DOCUMENTATION_CRITICAL_OPEN=$(gh issue list --repo $REPO --state open --label "T/documentation" --label "P/critical" --limit $LIMIT --json number | jq '. | length')
DOCUMENTATION_CRITICAL_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/documentation" --label "P/critical" --limit $LIMIT --json number | jq '. | length')

ENHANCEMENT_LOW_OPEN=$(gh issue list --repo $REPO --state open --label "T/enhancement" --label "P/low" --limit $LIMIT --json number | jq '. | length')
ENHANCEMENT_LOW_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/enhancement" --label "P/low" --limit $LIMIT --json number | jq '. | length')
ENHANCEMENT_MEDIUM_OPEN=$(gh issue list --repo $REPO --state open --label "T/enhancement" --label "P/medium" --limit $LIMIT --json number | jq '. | length')
ENHANCEMENT_MEDIUM_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/enhancement" --label "P/medium" --limit $LIMIT --json number | jq '. | length')
ENHANCEMENT_HIGH_OPEN=$(gh issue list --repo $REPO --state open --label "T/enhancement" --label "P/high" --limit $LIMIT --json number | jq '. | length')
ENHANCEMENT_HIGH_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/enhancement" --label "P/high" --limit $LIMIT --json number | jq '. | length')
ENHANCEMENT_CRITICAL_OPEN=$(gh issue list --repo $REPO --state open --label "T/enhancement" --label "P/critical" --limit $LIMIT --json number | jq '. | length')
ENHANCEMENT_CRITICAL_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/enhancement" --label "P/critical" --limit $LIMIT --json number | jq '. | length')

QUESTION_LOW_OPEN=$(gh issue list --repo $REPO --state open --label "T/question" --label "P/low" --limit $LIMIT --json number | jq '. | length')
QUESTION_LOW_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/question" --label "P/low" --limit $LIMIT --json number | jq '. | length')
QUESTION_MEDIUM_OPEN=$(gh issue list --repo $REPO --state open --label "T/question" --label "P/medium" --limit $LIMIT --json number | jq '. | length')
QUESTION_MEDIUM_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/question" --label "P/medium" --limit $LIMIT --json number | jq '. | length')
QUESTION_HIGH_OPEN=$(gh issue list --repo $REPO --state open --label "T/question" --label "P/high" --limit $LIMIT --json number | jq '. | length')
QUESTION_HIGH_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/question" --label "P/high" --limit $LIMIT --json number | jq '. | length')
QUESTION_CRITICAL_OPEN=$(gh issue list --repo $REPO --state open --label "T/question" --label "P/critical" --limit $LIMIT --json number | jq '. | length')
QUESTION_CRITICAL_CLOSED=$(gh issue list --repo $REPO --state closed --label "T/question" --label "P/critical" --limit $LIMIT --json number | jq '. | length')

echo "----------------------------------------------------------------------------------------------"
echo "$REPO issues statistics"
echo "----------------------------------------------------------------------------------------------"
echo "Open issues:   $OPEN"
echo "Closed issues: $CLOSED"
echo "Total issues:  $(($OPEN + $CLOSED))"
echo

echo "BUGS (open/closed)"
printf "%-10s %10s %10s %10s\n" "Low" "Medium" "High" "Critical"
printf "%-10s %10s %10s %10s\n" "----------" "----------" "----------" "----------"
printf "%-10s %10s %10s %10s\n" "$BUG_LOW_OPEN/$BUG_LOW_CLOSED" "$BUG_MEDIUM_OPEN/$BUG_MEDIUM_CLOSED" "$BUG_HIGH_OPEN/$BUG_HIGH_CLOSED" "$BUG_CRITICAL_OPEN/$BUG_CRITICAL_CLOSED"
echo
echo "DOCUMENTATION (open/closed)"
printf "%-10s %10s %10s %10s\n" "Low" "Medium" "High" "Critical"
printf "%-10s %10s %10s %10s\n" "----------" "----------" "----------" "----------"
printf "%-10s %10s %10s %10s\n" "$DOCUMENTATION_LOW_OPEN/$DOCUMENTATION_LOW_CLOSED" "$DOCUMENTATION_MEDIUM_OPEN/$DOCUMENTATION_MEDIUM_CLOSED" "$DOCUMENTATION_HIGH_OPEN/$DOCUMENTATION_HIGH_CLOSED" "$DOCUMENTATION_CRITICAL_OPEN/$DOCUMENTATION_CRITICAL_CLOSED"
echo
echo "ENHANCEMENT (open/closed)"
printf "%-10s %10s %10s %10s\n" "Low" "Medium" "High" "Critical"
printf "%-10s %10s %10s %10s\n" "----------" "----------" "----------" "----------"
printf "%-10s %10s %10s %10s\n" "$ENHANCEMENT_LOW_OPEN/$ENHANCEMENT_LOW_CLOSED" "$ENHANCEMENT_MEDIUM_OPEN/$ENHANCEMENT_MEDIUM_CLOSED" "$ENHANCEMENT_HIGH_OPEN/$ENHANCEMENT_HIGH_CLOSED" "$ENHANCEMENT_CRITICAL_OPEN/$ENHANCEMENT_CRITICAL_CLOSED"
echo
echo "QUESTION (open/closed)"
printf "%-10s %10s %10s %10s\n" "Low" "Medium" "High" "Critical"
printf "%-10s %10s %10s %10s\n" "----------" "----------" "----------" "----------"
printf "%-10s %10s %10s %10s\n" "$QUESTION_LOW_OPEN/$QUESTION_LOW_CLOSED" "$QUESTION_MEDIUM_OPEN/$QUESTION_MEDIUM_CLOSED" "$QUESTION_HIGH_OPEN/$QUESTION_HIGH_CLOSED" "$QUESTION_CRITICAL_OPEN/$QUESTION_CRITICAL_CLOSED"

