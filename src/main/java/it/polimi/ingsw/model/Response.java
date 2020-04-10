package it.polimi.ingsw.model;

public enum Response {

    //Response for match beginning

    CHALLENGERCHOICE,

    //Response for game logic
    NEWMOVE,MOVED,BUILD,NOTMOVED,NOTBUILD,NOTBUILDPLACE,NEWBUILD,ASSIGNCONSTRAINT,WIN, BUILDWIN, NOTWIN,NOTBUILDWIN,TOMOVE,BUILDBEFORE,BUILDEDBEFORE,LOOSE,WINMISMATCH,

    ENDTURN,ASSIGNEDCONSTRAINT,


    //Response for game config

    PLAYERADDED,NICKUSED,GAMESTARTED,REMOVEDPLAYER

}
