import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Views.CountriesTab;
import Views.GamesTab;
import Views.GenresTab;
import Views.PublishersTab;

public class MainFrame extends JFrame {
	private JTabbedPane tab = new JTabbedPane();

	private GamesTab gamesTab = new GamesTab();
	private PublishersTab publishersTab = new PublishersTab();
	private GenresTab genresTab = new GenresTab();
	private CountriesTab countriesTab = new CountriesTab();

	public MainFrame() {
		this.setTitle("Game Store");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		tab.add(gamesTab.getPanel(), "Games");
		tab.add(publishersTab.getPanel(), "Publishers");
		tab.add(genresTab.getPanel(), "Genres");
		tab.add(countriesTab.getPanel(), "Countries");

		tab.addChangeListener(getTabChangeListener());

		this.add(tab);

		gamesTab.loadData();
	}

	private ChangeListener getTabChangeListener() {
		return new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				switch (index) {
				case 0:
					gamesTab.loadData();
					break;
				case 1:
					publishersTab.loadData();
					break;
				case 2:
					genresTab.loadData();
					break;
				case 3:
					countriesTab.loadData();
					break;
				}
			}
		};
	}
}
