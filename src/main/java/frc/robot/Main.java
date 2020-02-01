/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
* Do NOT add any static variables to this class, or any initialization at all. Unless you know what
* you are doing, do not modify this file except to change the parameter class to the startRobot
* call.
*/
public final class Main {
	private Main() {
	}
	
	/**
	* Main initialization function. Do not perform any initialization here.
	*
	* <p>If you change your main robot class, change the parameter type.
	*/
	public static void main(String... args) {
		RobotBase.startRobot(Robot::new);
	}
}

// .......------::::://////+++oooossssssyyyyyyhhhhhhhdddddddddddddddddddmmmmmmmmmmmmmddmmdddddddddhhhhhhhhhhhhhhhdddddhhhhh
// -------:::::://////+++++oooosssssyyyyyyhhhhhhhdddhddddddddmmmmmmmmmmmmmmmmmmmmmmmmmmmmdddddddddhhhhhhhhhhddhhhdddhhhhhhh
// ::::::////////++++++oooosssssssyyyyyyhhyysssssssssyyydddddmmmmmmmmmddddddmmmddmdddddmmddhdddddddhhhhhhhhhdhddddhhhhhhhhh
// ++/////+++++++oooooosssssyyyysyyyyyyyssooooosyhhdhhdddddddddddddddddddddddddddddddddddddddhhdddhhhhhhhhhdddddddhhhhhhhhh
// ++++++ooossooooooossssyyyyyyyyyyyhhhhhhhhhhhhhddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddhhhyyyyhhh
// +oooosyyyyyyyysssssssyyyyyhhyyhhhhhhhhhhhhhhhdddddddddddddddddddddmdddddddddddddddddddddddddddddddddddddddddddhyyyyyyyhh
// ++/+oyhhhhhyysssssyyyyhhhhdhyhhhhhhhhhhddddddddddddddddddddddddddddmdddddmmdmmmmddddddddddddddddddddddddddddddyyssssyyhh
// :///+ydddhhhhhhhyhhhhhdddddddddddddddddddddddddddddddddddddddddddddmdmddddmmmddddddddddddddddddddddddhhhdddddhyssssssyyy
// /+o+ydddddddhhhhhhhhhhdddddddddddddddddddddddddddddddddddddddddddddmdmddmmmdddddddddddddddddddddddddddddhddddhsssssssyyy
// oosydddddddddhhhhhhhhhddmdddddddddddddddddddddddddmmmddmmmmmmmmmddmmdmmmmmdddddddddddddddddddddddddhhhdddddddhsooooosyyy
// ddddddddddddhhhhyyyhhhddmdddddddddddddddddddddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdddddddddddddddddddddddddhdddddddysooooosyhy
// ooooooo+++++//////////+oydmmdddddddddddddddmddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdddddddddddddddddddddhdddddddyoooooosyhy
// ++///////:::::::::://///odmmdddddddddmmddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdddddddddysdddddddddddddddyoooooosyhh
// hhhhhhyyyyyyyyyyyyyyhhdhdmmmmmmmmmmmmmmmdmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmddddddddddddddddddddds+++++osyhh
// mmmmmmmmmmmmmmmmmmmmmmmmmmddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdddddddddddddddyo+++++syhh
// NNNmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdmmmmmdmddddddyo+++++shhh
// NNNmNNNNNNNNmmNmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmdmmmmmmmmmddmdy++++++shdh
// NNNNNNNNNNmNmmmmmmmmmmmmmmmmmmmmmmdddddddddddddmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmho+++++oyys
// NNNNNNmmNmmmmmmmmmmmmmmmmmmmmmmmddhyyyyyyyyyyyyyhhddmmmmmmmmmmmmmmmmmmNNmmmmmmmmmmmmmNNNNNmNNNNmmmmmmmmmmmmmmho+++++shhy
// mmmmmmmmmmmmmdmdddddmmmmmmmmmmmdhyyssssssoooooo+oosyhddmmmmmmmmmmmmNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNmmmmmmmmho+++++sdmm
// ddddhhhdhhhhhhhhhhhhhhddmmmmmmdyysssooo++/+///////++osyhddmmmmmmmNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNds+++++ymNN
// yyyyyyyyysssssssssssssyhmmmmmdyssoooo+//:/::::///+//++oossyddmmmNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNdo+///+ymNN
// sssssoooo+++++++++++++oydmddmdhyysso++/::::::/+ssyyssoooossyhddmmNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNh+////+ymNN
// sssoooooo++++++//////++shdddddddddhyso/::---:+ossyyhddhyyyyyhhhhdmNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNms/////+hNNN
// o+++++++++////////////+ohddddhhhhyyys+/::----:////+oosyhhyyyyhhdddmNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNd+/////+hNNN
// ++++///////::::::::/++syhdddhysoo++++/::------:--------::///++oyhddmmmNNNNNNNNNNNNNNMMMMMMMNNNNMMNNNNMMMNMNNd+/:///+hNNN
// ++///////:::::::::/yhhhhyhysooossso+/:::-----:://:::::---.----://osyhhhhddhdddmNNNMMMMMMMMMMMNMMMMMMMMMMMMMNh+/////+hNNN
// ++++/////::::::---:shyyhhyso++:::::+o+/:--:/+oo++++ooo+//++//:--:::/++++++++oshmNMMMMMMMMMMMMMMMMMMMMMMMMMMNy/::::/+dNNN
// //:::::::::::::----/o///+osss+++o/.-shyssss+:::-:::++:-...-/os/--::///:::://++sdNMMMMMMMMMMMNMMMMMMMMMMMMMNmo/:::::+dNNN
// ::::::::::::-------:/..-.-/oo++oo/-:oysosyo/:----:-::-:-````.+hsoo+ydddhyyyyysymNNNMMMMMMMMMMMMMMMMMMMMMMMNd+:::::/odNNN
// :::::::::::---------:....-:/:::::--/o+:--+o/-....-:---:-..-.-/ydhysydmmmNNNNNNNNMMMMMMMMMMMMMMMMMMMMMMMMMMNh/::::::odNMN
// :::::::::-----------:----::::-----:++:...:+/:..............--/yy++++oooosssyyhmNMMMMMNMMMMMMMMNNNMMMMMMMMNms:---:::odNMM
// //:::::::---------..-::--::----..-/+:.``.-//:.`````......`.../o/---------:::/+ymNNMMMMNNNmddhyo+osdNMMMMNNdo:-:::::omNNN
// //:::::::------.....--/+//:-----://:.```..-:-.``````````````-/:..........---:/sdNNNNNNNNh+:--::////odNMMNmh+:---:::omMNN
// ////:::::----........--:////////::-.```....-:..````````````.::.````......--::/sdNNNNNNNd/--:/:--:///+dNNmdh+------:smMMN
// +++////::----...``...------........`````.--..-:-..```````.-:-.``````.....--:/+ymNNNNNNds/::-.```.---:smmdhy/------:smNNN
// ////:::::::---.......----...````..```````..--...-:::::::---.````````....---:/odNNNNNmhsoo/-.````.-..:sdddhy/------:smNNm
// ---.............```.....-..`````--.```....`.--``````````````````````....--:/+ymNNNmhs+++//:--.``..../hmdhhs:------:smNmd
// :::---.....`````````````.-..````.--...-:+/..--`````    `````````````...---:/+hmNNdyo/:::-.``...`...:ymmdhhs:------:ymNmh
// ++++/////::::-----.----.--..````.-....``......`` ````  `````````````...---::+sdmdy+/:---.````.``..:yNmdhhyo:------/ymNmd
// ++////////:::::::--::///:--..``....`````````````     ````````````````...---:/syhyo+//:-.....```.-+dNmdhhyyo-.-----/yNNmd
// ++++//////::::------:::::--.......``````````````    ````````````````.....--:+osso+/-.````````../ymNNmdhyyy+-.-----/hNNmm
// ////////////:::-------::/:-..--........``````````` ````````````````.......-:/++++:-.```````../sdNNNmdhhyys/-..----:shhhh
// :::::::////::::-------:://-..---::::-----...```````````````````````````..--::/++/:..```..:+sdNNMNNmmdhyyys/......-://:::
// ::////:-::::::-------::://:...-:+oooooooo+/::-.....````````````````````..--://+ossoooooshmNNNMNNNmddhhyyys:......-://:::
// ::::://:--:::----------:///-...-:/++oooooooo+++:-...``````````````````...--:/+osyyhmNNNNNNNNNmmmmdddhyysso:......-+yhyys
// ::::://::--::--....--://+oo:....--.....-------..``````````````````````...-::/+ossshmNNNNNNmmddddddddhyysso-......-omNMNN
// //::::::::---....--:/+oosss+-......``````....`````````````````````````...-:/+ossoydNNNNNNmmdddddddhhyyssso:--....-omNNNN
// :///:::///::--.-:/+oossssyss/-......```````````````  `````````````````..-::/+ssoshmNNNNNNNmmddddddhhhyyyyysoo/-..:smNNNN
// yhdddhhhhhyyso+/oossysyyyyyyo:..``````````````````   ````````````````...-:/+osoosdmNNNNNNmmmmmmdddhhhyyyyyyyys/--:smNNNN
// mmmmddddddhhysossyhhhyyyyyyys/-..```````` `   ````   ``````````````....-::/+ooooydNNNNNmmmmmmmdhhyyyssssssyyyyy/-:smNNNN
// mmmmdddddddhsssyhhhhhhhhyyhyy+-..`````````````````   `````````````...--::/+oo++ohmNNNNNmmmdhyssoooosssssssyyyyys++ymNNNN
// mmmdddddddhyosyhdddddhhhhhhhyo-..````````````````   `````````````..--:://+o++/+shmNNNmmdyso++ooosooossssssyyyyyhhdmNNNNN
// mmmdddddddhsoshdddddddhhhhhhhy:...`````````````   `````````````...-::/++oo+/:/+shmmdhsooo++++oooooooossyyyyhhhhhhdmmmmmm
// mmmdddddhdyooshddddddddhhhhhhh+-..`````````````  ```````````...-::/++ooo/::-:/+sssoo++++++o+++ooooossyyyhyyyhhhhhdmmmmmm
// mddhy+/:/+soooydmmdddddhdhhhhhy/...``````````````````````...-::/++ooo+/:----:/++++++++++++++oooossssyyyhddmmmmddddmmmmmm
// dy+:..-:+yysoooydmddddddddhhhhhy+-.....``````````````..--::/++ooo++/:-...--:////////++++++oooooooossyhdddddmmmdddmmmmmmd
// +-....:ydddysoooydmdddddddhhhhdddhso/:---.........--::/+++++///:--.....--://///+++////++++++++oosyyhhyyyyyssyhdddmmmdddd
// -...-+yddddhyso+oshdddddddhhhhdddddhho::///////++++++///::---........--:://///////++++++++++oosyyyyssssooooossshdmmddddd
// ..-/shdddddhhyso+ooshddddddhhhddddddho:--://///:::::---.......```..-://::://///+++++++++oossyssssoooooooooooooshdddddddd
// --/yyhdddhyyhdhyoo++oyhddddhhhddddhhy+--.-::------.........````.--://///:::///////+++ossyysooo++++++++oooo++osyhddddhddd
// :oyyhhhhhyyhddmdhso++oosyddhhhhdddhys+-....-.........````````.-:///////++//////+/+osssso++//++//+++oossssoosyhddhhhhhhhd
// oyhhhhhhhyhhddddhhyso++++syhhhhdddyos+:.......````````````.--::://///////////+osyysso+++/////+ossyyhhyyyyyyyyyyyyysooooo
// syhhhhhhhhhhdhhhyysyyso++++oshhhdhoss+:-.....``````````.--::///::////:///++osyysso++++//:/+oyhhhhyyssssoosssssssoo++++//
// yyhhddhhdhhhhyyyyssyyhys+++//+syyooso/+++//:::-.````.--:/:-:://///::///+osssso++//://:::/+shhyssooooo++/+++//+++++++////
// hhhdddhhhhyyyyysssssyyhhyssso++/++ssssysssssyyyso++oo+//://////:::://+ooooo++///:::::::+syyysooo++++++++/////////+++////

