package com.turku.route;

import java.util.List;

import com.turku.historydatabase.DBAdapterLanguage;

public class AlertDialogueAdapter {
    static String app_name_route = "ROUTE LIST";
    static String app_name_map = "ROUTE ON MAP";
    static String app_name = "TURKU ROUTE FINDER";
    static String app_name_route_detail = "ROUTE DETAIL";
    static String app_name_select_address = "SELECT ADDRESS FROM MAP";

    static String from_label = "From";
    static String maEditFromHint = "Enter Starting address";
    static String from_empty = "Starting Position is Empty.";

    static String to_label = "To";
    static String maEditToHint = "Enter Destination address";
    static String to_empty = "Destination Position is Empty.";

    static String my_location = "My Location";
    static String my_location_change = "My Locatio";

    static String departure_string = "DEPARTURE";
    static String arrival_string = "ARRIVAL";

    static String today_string = "TODAY";
    static String divider_string = "|";
    static String now_string = "NOW";

    static String find_route = "FIND ROUTE";

    static String delete_title = "Delete Row";
    static String delete_all = "Delete All";
    static String delete = "Delete";

    static String cmarginalTitle = "Change Marginal";
    static String cmarginal = "Time to change ";
    static String min = "min";

    static String walk = "WALK";

    static String InternetAlertDialogTitle = "No Internet Connection";
    static String InternetAlertDialogMessage = "You don't have internet connection.";
    static String Internetslow = "You don't have internet connection or might be very slow.";
    static String OK = "OK";

    static String waiting_for_location = "Waiting for location.....";

    static String GPSAlertDialogMessage = "GPS is not enabled. Do you want to go to setting menu?";
    static String GPSAlertDialogTitle = "GPS is setting";
    static String settings = "Go to Setting";
    static String cancel = "Cancel";

    static String progress = "Loading route...Please Wait...";
    static String toastForMapAddress = "Press and hold for getting the address.";
    static String toastForHistory = "History is enabled. Press history button to show it.";
    static String toastDelete = "Press and hold to delete history.";
    static String toast_taplist = "Click on list to see the detail.";

    static String nodestination = "Destination address ";
    static String nostarting = "Starting address ";
    static String notinMatka = " is not in Matka database.";
    static String multipledestination = "Multiple destination address found.";
    static String multiplestarting = "Multiple starting address found.";
    static String onlyway = "Only way to reach destination is by walking few steps.";
    static String select_startingadd = "Select Starting Address";
    static String select_endadd = "Select Destination Address";
    static String restart_app = "Restart the app to see the change.";

    static String google_playservices = "Google Play Services must be installed.";
    static String recquire_googleaccount = "This application requires a Google account.";
    static String device_notsupported = "This device is not supported.";

    static String Optimize_route = "Optimize Route";
    static String walking_speed = "Walking Speed";
    static String numberofroute = "Number of route";
    static String home = "Home";

    static String[] walkspeed = new String[] { "1=Slow (30m/min)",
            "2=Normal (70m/min)", "3=Fast (100m/min)", "4=Running (200m/min)",
            "5=Cycling (300m/min)" };
    static String[] routingNumber = new String[] { "2 Routes", "3 Routes",
            "5 Routes" };
    static String[] routeOptimize = new String[] { "1=Default", "2=Fastest",
            "3=Minimize changes", "4=Minimize walks" };
    static String[] drawerlistitem = new String[] { "Home", "Optimize Route",
            "Set Change Marginal", "Set Walking Speed", "Number of Route" };

    public AlertDialogueAdapter() {
        List<String> lan = DBAdapterLanguage.getAllData();

        if (lan.get(0).equals("FIN")) {
            app_name_route = "REITIT";
            app_name_map = "REITTI KARTALLA";
            app_name = "TURKU REITTIOPAS";
            app_name_route_detail = "REITIN TIEDOT";
            app_name_select_address = "VALITSE OSOITE KARTALTA";

            from_label = "Mistä";
            maEditFromHint = "Lähtöosoite";
            from_empty = "Lähtöosoite on tyhjä.";

            to_label = "Mihin";
            maEditToHint = "Syötä määränpää";
            to_empty = "Määränpää on tyhjä.";

            my_location = "Oma sijainti";
            my_location_change = "Oma sijainti";

            departure_string = "LÄHTÖ";
            arrival_string = "SAAPUMINEN";

            today_string = "TÄNÄÄN";
            divider_string = "|";
            now_string = "NYT";

            find_route = "ETSI REITTI";

            select_startingadd = "Valitse alkaen osoite";
            select_endadd = "Valitse lähtöosoite";
            restart_app = "Käynnistä sovellus uudelleen nähdäksesi muutokset";
            delete_title = "Poista rivi";
            delete_all = "Poista kaikki";
            delete = "Poista";

            cmarginalTitle = "Vaihtomarginaali";
            cmarginal = "Bussin vaihtoaika ";
            min = " min";

            walk = "KÄVELE";

            InternetAlertDialogTitle = "Ei internet-yhteyttä";
            InternetAlertDialogMessage = "Internet-yhteys ei ole käytettävissä.";
            Internetslow = "Internet-yhteys hidas tai sitä ei ole.";
            OK = "Ok";

            waiting_for_location = "Odotetaan sijaintia...";

            GPSAlertDialogMessage = "GPS ei ole aktivoitu. Siirrytäänkö asetuksiin?";
            GPSAlertDialogTitle = "GPS KÄYNNISTYMÄSSÄ";
            settings = "Siirry asetuksiin";
            cancel = "Peruuta";

            progress = "Ladataan reittiä, odota...";
            toastForMapAddress = "Paina pitkään saadaksesi osoitteen.";
            toastForHistory = "Historia on käytössä. Paina historia-painiketta selataksesi sitä.";
            toastDelete = "Paina pitkään poistaaksesi historian.";
            toast_taplist = "Paina listaa nähdäksesi lisätietoja.";


            nodestination = "Määränpää ";
            nostarting = "Lähtöosoite ";
            notinMatka = " ei löydy Matkan tietokannasta.";
            multipledestination = "Useita osoitteita löydetty määränpäälle.";
            multiplestarting = "Useita lähtöosoitteita löydetty.";
            onlyway = "Määränpää saavutettavissa vain kävellen.";

            walkspeed = new String[]{
                    "Hidas (30m/min)",
                    "Tavallinen (70m/min)",
                    "Nopea (100m/min)",
                    "Juosten (200m/min)",
                    "Pyörällä (300m/min)"
            };
            routingNumber = new String[]{
                    "Kaksi reittiä",
                    "Kolme reittiä",
                    "Viisi reittiä"
            };
            routeOptimize = new String[]{
                    "Oletus",
                    "Nopein",
                    "Vähiten vaihtoja",
                    "Vähiten kävelyä"
            };
            drawerlistitem = new String[]{
                    "Alku",
                    "Optimoi reitti",
                    "Vaihtomarginaali",
                    "Kävelynopeus",
                    "Reittien määrä"
            };

            google_playservices = "Asenna Google Play-palvelut käyttääksesi sovellusta.";
            recquire_googleaccount = "Sovellus tarvitsee Google-tilin.";
            device_notsupported = "Tätä laitetta ei tueta.";
            Optimize_route = "Optimoi reitti";
            walking_speed = "Kävelynopeus";
            numberofroute = "Reittien määrä";
            home = "Alku";
        }
        if (lan.get(0).equals("SWE")) {
            app_name_route = "RUTTLISTA";
            app_name_map = "RUTTEN PÅ KARTAN";
            app_name = "TURKU ROUTE FINDER";
            app_name_route_detail = "RUTTUPPGIFTER";
            app_name_select_address = "VÄLJ ADRESS FRÅN KARTAN";

            from_label = "Från";
            maEditFromHint = "Mata avgångsadress";
            from_empty = "Ingen avgångsadress matad.";

            to_label = "Till";
            maEditToHint = "Mata destinationsadress";
            to_empty = "Ingen destination matad.";

            my_location = "Min position";
            my_location_change = "Min position";

            departure_string = "AVGÅNG";
            arrival_string = "ANKOMST";

            today_string = "IDAG";
            divider_string = "|";
            now_string = "NU";

            find_route = "SÖK RUTT";

            select_startingadd = "Välj avgångsadress";
            select_endadd = "Välj destinationsadress";
            restart_app = "Starta om appet för att ta ibruk inställningen.";
            delete_title = "Radera rad";
            delete_all = "Radera alla";
            delete = "Radera";

            cmarginalTitle = "Bytesmarginal";
            cmarginal = "Tid mellan busbyten ";
            min = " min";

            walk = "GÅ";

            InternetAlertDialogTitle = "Ingen internetanslutning";
            InternetAlertDialogMessage = "Ingen åtkomst till internet.";
            Internetslow = "Ingen internetanslutning eller långsam anslutning.";
            OK = "Ok";

            waiting_for_location = "Väntar på position...";

            GPSAlertDialogMessage = "GPS frånkopplad. Vill du gå till inställningarna?";
            GPSAlertDialogTitle = "Koppla på GPS";
            settings = "Gå till inställningarna";
            cancel = "Avbryt";

            progress = "Laddar rutt, vänta...";
            toastForMapAddress = "Tryck och håll för att välja adress.";
            toastForHistory = "Historiken är aktiverad. Välj historik för att granska.";
            toastDelete = "Tryck och håll för att radera historiken.";
            toast_taplist = "Tryck på listan för detaljer.";


            nodestination = "Destinationsadressen ";
            nostarting = "Avgångsadressen ";
            notinMatka = " finns inte i Matkas databas.";
            multipledestination = "Flera destinationsadresser hittades.";
            multiplestarting = "Flera avgångsadresser hittades.";
            onlyway = "Ända sättet att nå destinationen är till fots.";

            walkspeed = new String[]{
                    "Långsamt (30m/min)",
                    "Normalt (70m/min)",
                    "Snabbt (100m/min)",
                    "Springa (200m/min)",
                    "Cykla (300m/min)"
            };
            routingNumber = new String[]{
                    "Två rutter",
                    "Tre rutter",
                    "Fem rutter"
            };
            routeOptimize = new String[]{
                    "Standard",
                    "Snabbast",
                    "Minst byten",
                    "Minst fotgång"
            };
            drawerlistitem = new String[]{
                    "Hem",
                    "Optimera rutten",
                    "Bytesmarginal",
                    "Gånghastighet",
                    "Antal rutter"
            };
            google_playservices = "Du måste installera Google Play-tjänster.";
            recquire_googleaccount = "Appen kräver ett Google-konto.";
            device_notsupported = "Denna enhet stöds inte.";

            Optimize_route = "Optimera rutten";
            walking_speed = "Gånghastighet";
            numberofroute = "Antal rutter";
            home = "Hem";
        }
        if (lan.get(0).equals("ENG")) {
            app_name_route = "ROUTE LIST";
            app_name_map = "ROUTE ON MAP";
            app_name = "TURKU ROUTE FINDER";
            app_name_route_detail = "ROUTE DETAIL";
            app_name_select_address = "SELECT ADDRESS FROM MAP";

            from_label = "From";
            maEditFromHint = "Enter Starting address";
            from_empty = "Starting Position is Empty.";

            to_label = "To";
            maEditToHint = "Enter Destination address";
            to_empty = "Destination Position is Empty.";

            my_location = "My Location";
            my_location_change = "My Locatio";

            departure_string = "DEPARTURE";
            arrival_string = "ARRIVAL";

            today_string = "TODAY";
            divider_string = "|";
            now_string = "NOW";

            find_route = "FIND ROUTE";

            select_startingadd = "Select Starting Address";
            select_endadd = "Select Destination Address";
            restart_app = "Restart the app to see the change.";
            delete_title = "Delete Row";
            delete_all = "Delete All";
            delete = "Delete";

            cmarginalTitle = "Change Marginal";
            cmarginal = "Time to change ";
            min = " min";

            walk = "WALK";

            InternetAlertDialogTitle = "No Internet Connection";
            InternetAlertDialogMessage = "You don't have internet connection.";
            Internetslow = "You don't have internet connection or might be very slow.";
            OK = "OK";

            waiting_for_location = "Waiting for location.....";

            GPSAlertDialogMessage = "GPS is not enabled. Do you want to go to setting menu?";
            GPSAlertDialogTitle = "GPS is setting";
            settings = "Go to Setting";
            cancel = "Cancel";

            progress = "Loading route...Please Wait...";
            toastForMapAddress = "Press and hold for getting the address.";
            toastForHistory = "History is enabled. Press history button to show it.";
            toastDelete = "Press and hold to delete history.";
            toast_taplist = "Click on list to see the detail.";

            nodestination = "Destination address ";
            nostarting = "Starting address ";
            notinMatka = " is not in Matka database.";
            multipledestination = "Multiple destination address found.";
            multiplestarting = "Multiple starting address found.";
            onlyway = "Only way to reach destination is by walking few steps.";

            walkspeed = new String[] { "1=Slow (30m/min)",
                    "2=Normal (70m/min)", "3=Fast (100m/min)",
                    "4=Running (200m/min)", "5=Cycling (300m/min)" };
            routingNumber = new String[] { "2 Routes", "3 Routes", "5 Routes" };
            routeOptimize = new String[] { "1=Default", "2=Fastest",
                    "3=Minimize changes", "4=Minimize walks" };
            drawerlistitem = new String[] { "Home", "Optimize Route",
                    "Set Change Marginal", "Set Walking Speed",
                    "Number of Route" };

            Optimize_route = "Optimize Route";
            walking_speed = "Walking Speed";
            numberofroute = "Number of route";
            home = "Home";
            google_playservices = "Google Play Services must be installed.";
            recquire_googleaccount = "This application requires a Google account.";
            device_notsupported = "This device is not supported.";
        }
    }
}
