#!/bin/bash

RED='\033[31;1m'
BOLD='\033[1m'
RESET='\033[0m'

welcome_message="Welcome to ${RED}checkers${RESET} by ${BOLD}@yannickschall${RESET} and ${BOLD}@ginakokoska${RESET}. Be sure to enable '${BOLD}xhost +${RESET}' for GUI.\n\n"
echo -e "$welcome_message"


read -p "What view do you want to select? (${BOLD}gui${RESET}/${BOLD}tui${RESET}): " selectview
SELVIEW="${selectview,,}"

if [[ "$SELVIEW" == "gui" ]]; then
export C4_UITYPE="gui"
elif [[ "$SELVIEW" == "tui" ]]; then
export C4_UITYPE="tui"
elif [[ "$SELVIEW" == "q" ]]; then
exit 1
else
export C4_UITYPE="tui"
fi

read -p "Do you want to enable REST VIEW API? (${BOLD}y${RESET}/${BOLD}n${RESET}): " selectrestview
SELRES="${selectrestview,,}"

if [[ "$SELRES" == "n" ]]; then
export C4_VIEWREST="n"
elif [[ "$SELRES" == "q" ]]; then
exit 1
else
export C4_VIEWREST="y"
fi


#sbt "run"