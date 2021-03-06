package gui;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import functions.APICaller;
import functions.KeyUpdater;

/**
 * Visualizer
 * 
 * A visual window (JFrame) that serves as the main screen for the program. Allows the user to update their API Key
 * and scrape Challonge for a Tournament ID. 
 */
@SuppressWarnings("serial")
public class AddTournamentVisualizer extends JFrame{

	public AddTournamentVisualizer(Connection con){
		//Set up the user preferences that store the API Key
		Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
		
		//Set up the visual window and all its content
		
		setTitle("Challonge Scraper");
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

		Font titleFont = new Font("Trebuchet MS", Font.BOLD, 40);
		Font subtitleFont = new Font("Trebuchet MS", Font.BOLD, 30);
		Font inputFont = new Font("Trebuchet MS", Font.BOLD, 20);
		
		
		JLabel title = new JLabel("Add Tournament to Database");
		title.setFont(titleFont);
		title.setAlignmentX(CENTER_ALIGNMENT);
		content.add(title);
		
		JLabel formatTitle = new JLabel("Tournament URLs are formatted as: [Subdomain (if applicable)].challonge.com/[Tournament ID])");
		formatTitle.setFont(inputFont);
		formatTitle.setAlignmentX(CENTER_ALIGNMENT);
		content.add(formatTitle);
		
		
		JLabel tournamentIDTitle = new JLabel("Tournament ID:");
		tournamentIDTitle.setFont(subtitleFont);
		tournamentIDTitle.setAlignmentX(CENTER_ALIGNMENT);
		content.add(tournamentIDTitle);

		
		JTextField tournamentID = new JTextField();
		tournamentID.setFont(inputFont);
		tournamentID.setAlignmentX(CENTER_ALIGNMENT);
		content.add(tournamentID);

		JLabel subDomainTitle = new JLabel("Organization Subdomain (if applicable):");
		subDomainTitle.setFont(subtitleFont);
		subDomainTitle.setAlignmentX(CENTER_ALIGNMENT);
		content.add(subDomainTitle);

		JTextField subDomain = new JTextField();
		subDomain.setFont(inputFont);
		subDomain.setAlignmentX(CENTER_ALIGNMENT);
		content.add(subDomain);

		
		JButton scrapeButton = new JButton("Add Tournament to Database");
		
		ActionListener scrape = new ActionListener(){
			//Scrapes the internet for a tournament's results
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//get the API key from preferences or an empty string if none exist.
				String key = prefs.get("apiKey", "");
					if(key.equals("")){
						//Makes sure that an API Key has been entered. If not, explain the situation with a pop-up.
						PopUpVisualizer e = new PopUpVisualizer("Error", 
												"You haven't entered an API key yet. Please enter an API key.");
						e.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						e.setVisible(true);
						return;
					}
					//Creates an APICaller
					APICaller a = new APICaller(key);
					//Checks if the tournament ID text box has anything in it
					if(!tournamentID.getText().isEmpty()){
						//If it does, create a ResultsVisualizer by scraping the internet for a tournament's match list.
						if(subDomain.getText().isEmpty()){
							//Case for no subdomain
							ResultsVisualizer r = new ResultsVisualizer(a.scrape(tournamentID.getText()), con);
							r.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
							r.setVisible(true);
							return;
						} else{
							ResultsVisualizer r = new ResultsVisualizer(a.scrape(subDomain.getText() + "-" + tournamentID.getText()), con);
							r.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
							r.setVisible(true);
							return;							
						}
					}else{
						//If it doesn't, explain the situation with a pop-up.
						PopUpVisualizer e = new PopUpVisualizer("Error", 
												"You haven't entered an Tournament ID. Please enter a Tournament ID.");
						e.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						e.setVisible(true);
						return;						
					}
				}
		};
		scrapeButton.addActionListener(scrape);
		scrapeButton.setFont(inputFont);
		scrapeButton.setAlignmentX(CENTER_ALIGNMENT);
		content.add(scrapeButton);
		
		JButton updateKeyButton = new JButton("Update API Key");
		
		ActionListener updateKey = new ActionListener(){
			//Opens up a KeyUpdater to allow the user to update their API Key.
			@Override
			public void actionPerformed(ActionEvent arg0){
				KeyUpdater updater = new KeyUpdater(prefs);
				updater.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				updater.setVisible(true);
			}
		};
		updateKeyButton.addActionListener(updateKey);
		updateKeyButton.setFont(inputFont);
		updateKeyButton.setAlignmentX(CENTER_ALIGNMENT);
		content.add(updateKeyButton);
						
		JButton closeButton = new JButton("Close Window");
		
		ActionListener closeWindow = new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0){
				AddTournamentVisualizer.super.dispose();
			}
		};
			closeButton.addActionListener(closeWindow);
			closeButton.setFont(inputFont);
			closeButton.setAlignmentX(CENTER_ALIGNMENT);
			content.add(closeButton);
		
		add(content);
		pack();
	}
}
