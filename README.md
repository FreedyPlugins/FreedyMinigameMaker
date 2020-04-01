# FreedyMinigameMaker
Make MiniGame finally


미니게임 생성하는 방법:
/fmg create <게임이름> <최대인원> <시작인원> <시작대기초> <종료대기초>

미니게임 삭제하는 방법:
/fmg remove <게임이름>

미니게임 참여하는 방법:
/fmg join [게임이름]

미니게임 퇴장하는 방법:
/fmg quit <게임이름>

미니게임 목록 보는 방법:
/fmg list

미니게임을 설정하는 방법:
/fmg set <게임이름> <start|end|addBlock|gameType|teamMode|maxHealth|timePerPlayer> ...

모든 플레이어들의 미니게임 시작 위치를 설정하는 방법
/fmg set <게임이름> start

레드팀의 플레이어들의 미니게임 시작 위치를 설정하는 방법
/fmg set <게임이름> start red

블루팀의 플레이어들의 미니게임 시작 위치를 설정하는 방법
/fmg set <게임이름> start blue

미니게임의 종료 위치를 설정하는 방법:
/fmg set <게임이름> end

미니게임의 종류를 설정하는 방법:
/fmg set <게임이름> gameType <hideAndSeek|zombieMode>

미니게임의 팀 시스템 도입 여부를 설정하는 방법:
/fmg set <게임이름> teamMode <true|false>

시작할 때 모든 플레이어들의 최대 생명력을 설정하는 방법:
/fmg set <게임이름>  maxHealth <체력>

시작할 때 블루팀의 플레이어들의 최대 생명력을 설정하는 방법:
/fmg set <게임이름> maxHealth <체력> blue

시작할 때 레드팀의 플레이어들의 최대 생명력을 설정하는 방법:
/fmg set <게임이름> maxHealth <체력> red

미니게임 종료 타이머의 양을 플레이 중인 플레이어 인원에 비례하여 설정하는 방법:
/fmg set <게임이름> timePerPlayer <플레이어수의비례한죵료타이머배수>


좀비서바이벌  제작 방법:
/fmg create <게임이름> <최대인원> <시작인원> <시작대기초> <종료대기초>
/fmg set <게임이름> gameType zombieMode
/fmg set <게임이름> maxHealth <체력> red
/fmg set <게임이름> teamMode false
미니게임이 끝날 때 이동 될 장소에서 /fmg set <게임이름> end
