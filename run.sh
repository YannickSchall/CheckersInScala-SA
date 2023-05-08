#!/bin/bash

#Colours
RED='\033[31;1m'
BOLD='\033[1m'
RESET='\033[0m'



#Welcome Message
welcome_message="Welcome to ${RED}CHECKERS${RESET} by ${BOLD}@yannickschall${RESET} and ${BOLD}@ginakokoska${RESET}.
Be sure to enable '${BOLD}xhost +${RESET}' for GUI."

printf "$welcome_message"

#Select View
printf "What View do you want to select? (${BOLD}gui${RESET}/${BOLD}tui${RESET}): "
read -t 15 selectview



SELVIEW=${selectview,,}

if [[ $SELVIEW == "gui" ]]; then
  export C4_UITYPE="gui"
elif [[ $SELVIEW == "tui" ]]; then
  export C4_UITYPE="tui"
elif [[ $SELVIEW == "q" ]]; then
  exit 1
else
  export C4_UITYPE="gui"
fi

#Select IF REST TUI API should be online
printf "Do you want to enable REST VIEW API? (${BOLD}y${RESET}/${BOLD}n${RESET}): "
read -t 15 selectrestview

SELRES=${selectrestview,,}

if [[ $SELRES == "n" ]]; then
  export C4_VIEWREST="n"
elif [[ $SELRES == "q" ]]; then
  exit 1
else
  export C4_VIEWREST="y"
fi

#RUN sbt
sbt "run"
