package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;
	JMenuItem mnuItemCloseTicket;

	public Tickets(Boolean isAdmin) {

		chkIfAdmin = isAdmin;
		createMenu();
		prepareGUI();

	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below
		mnuItemCloseTicket = new JMenuItem("Close Ticket");
		mnuTickets.add(mnuItemCloseTicket);
		
		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);
		mnuItemCloseTicket.addActionListener(this);

		/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above
		 */

	}

	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuTickets);
		// add menu bar components to frame
		if (chkIfAdmin == true) {
			bar.add(mnuAdmin);
		} else {
		}
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
				System.out.println("The application has been terminated.");
			}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {

			// get ticket information
			String ticketName = JOptionPane.showInputDialog(null, "Enter your name");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

			// insert ticket information to database

			int id = dao.insertRecords(ticketName, ticketDesc);

			// display results if successful or not to console / dialog box
			if (id != 0) {
				System.out.println("Ticket ID : " + id + " created successfully!!!");
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
			} else
				System.out.println("Ticket cannot be created!!!");
		}

		else if (e.getSource() == mnuItemViewTicket) {

			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords()));
				jt.setBounds(30, 40, 200, 400);
				JScrollPane sp = new JScrollPane(jt);
				add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above
		 */
		else if (e.getSource() == mnuItemUpdate) {
			try {
				String ticketID = JOptionPane.showInputDialog(null, "Enter ticket ID to update");

				String[] select = { "Update Name", "Update Description" };
				String userInput = (String) JOptionPane.showInputDialog(null, "Choose a category to be updated",
						"Update a Ticket", JOptionPane.QUESTION_MESSAGE, null, select, select[0]);

				String option, updatedTicket;
				if (userInput.equals("Update Name")) {
					option = "Ticket_Issuer";
					updatedTicket = JOptionPane.showInputDialog(null, "Enter a new name");
				}

				else {
					option = "Ticket_Description";
					updatedTicket = JOptionPane.showInputDialog(null, "Enter new description");
				}

				dao.updateRecords(ticketID, option, updatedTicket);
				JOptionPane.showMessageDialog(null, "Ticket ID: " + ticketID + " was updated.");
			}

			catch (Exception se) {
				se.printStackTrace();
			}
		}

		else if (e.getSource() == mnuItemDelete) {
			try {

				String ticketID = JOptionPane.showInputDialog(null, "Enter ticket ID to delete");

				int userInput = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this ticket?",
						"Delete Ticket", JOptionPane.YES_NO_OPTION);
				if (userInput == JOptionPane.YES_OPTION) {
					dao.deleteRecords(ticketID); 
					JOptionPane.showMessageDialog(null, "Ticket ID: " + ticketID + "has been deleted");
					System.out.println("Ticket has been deleted");
				} else {
					dispose();
				}
			}

			catch (Exception se) {
				se.printStackTrace();
			}
		}

		else if (e.getSource() == mnuItemCloseTicket) {

			try {
				String ticketID = JOptionPane.showInputDialog(null, "Enter Ticket ID to be closed");

				int userInput = JOptionPane.showConfirmDialog(null, "Are you sure to close this ticket?",
						"Close Ticket", JOptionPane.YES_NO_OPTION);
				if (userInput == JOptionPane.YES_OPTION) {
					dao.closeRecords(ticketID);
					JOptionPane.showMessageDialog(null, "Ticket ID: " + ticketID + " has been closed");
					System.out.println("Ticket has been closed");
				} else {
					dispose();
				}
			} catch (Exception se) {
				se.printStackTrace();
			}

		}

	}

}
