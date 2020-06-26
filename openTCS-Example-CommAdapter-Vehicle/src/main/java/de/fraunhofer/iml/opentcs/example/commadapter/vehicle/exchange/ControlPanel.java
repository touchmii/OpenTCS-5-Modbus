/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;
import javax.swing.border.*;
import com.google.common.base.Strings;
import com.google.inject.assistedinject.Assisted;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.ExampleProcessModel;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.commands.SendRequestCommand;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.commands.SetDisconnectingOnVehicleIdleCommand;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.commands.SetIdleTimeoutCommand;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.commands.SetLoggingEnabledCommand;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.commands.SetReconnectingOnConnectionLossCommand;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.commands.SetVehicleHostCommand;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.commands.SetVehiclePortCommand;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.OrderRequest;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import javax.inject.Inject;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.opentcs.components.kernel.services.VehicleService;
import org.opentcs.customizations.ServiceCallWrapper;
import org.opentcs.data.model.Point;
import org.opentcs.drivers.vehicle.AdapterCommand;
import org.opentcs.drivers.vehicle.VehicleProcessModel;
import org.opentcs.drivers.vehicle.management.VehicleCommAdapterPanel;
import org.opentcs.drivers.vehicle.management.VehicleProcessModelTO;
import org.opentcs.util.CallWrapper;
import org.opentcs.util.Comparators;
import org.opentcs.util.gui.StringListCellRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the gui of the vehicle control and visualizes the status of protocol data.
 *
 * @author Mats Wilhelm (Fraunhofer IML)
 */
public class ControlPanel
    extends VehicleCommAdapterPanel {

  /**
   * This class' logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(ControlPanel.class);
  /**
   * Declares how many orders should be saved.
   */
  private static final int MAX_LAST_ORDERS = 20;
  /**
   * Model for JList.
   */
  private final DefaultListModel<OrderRequest> lastOrderListModel = new DefaultListModel<>();
  /**
   * The vehicle service used for interaction with the comm adapter.
   */
  private final VehicleService vehicleService;
  /**
   * The call wrapper to use for service calls.
   */
  private final CallWrapper callWrapper;
  /**
   * The comm adapter's process model.
   */
  private ExampleProcessModelTO processModel;

  /**
   * Creates a new instance.
   *
   * @param processModel The comm adapter's process model
   * @param vehicleService The vehicle service
   * @param callWrapper The call wrapper to use for service calls
   */
  @Inject
  public ControlPanel(@Assisted ExampleProcessModelTO processModel,
                      @Assisted VehicleService vehicleService,
                      @ServiceCallWrapper CallWrapper callWrapper) {
    this.processModel = requireNonNull(processModel, "processModel");
    this.vehicleService = requireNonNull(vehicleService, "vehicleService");
    this.callWrapper = requireNonNull(callWrapper, "callWrapper");
    initComponents();
    initComboBoxes();
    initGuiContent();
  }

  /**
   * Initializes combo boxes for destinations and actions.
   */
  private void initComboBoxes() {
    try {
      // Initialize the list of known points. Only add points whose names have a length of 8.
      destinationComboBox.removeAllItems();
      callWrapper.call(() -> vehicleService.fetchObjects(Point.class)).stream()
          .sorted(Comparators.objectsByName())
          .forEach(point -> destinationComboBox.addItem(point));
      // Initialize the list of valid actions.
      actionComboBox.removeAllItems();
      for (OrderRequest.OrderAction curAction : OrderRequest.OrderAction.values()) {
        actionComboBox.addItem(curAction);
      }
    }
    catch (Exception ex) {
      LOG.warn("Error fetching points", ex);
    }
  }

  /**
   * Updates all fields showing an attribute of the process model to the current state
   */
  private void initGuiContent() {
    // Trigger an update for all attributes once first.
    for (VehicleProcessModel.Attribute attribute : VehicleProcessModel.Attribute.values()) {
      processModelChange(attribute.name(), processModel);
    }
    for (ExampleProcessModel.Attribute attribute : ExampleProcessModel.Attribute.values()) {
      processModelChange(attribute.name(), processModel);
    }
  }

  @Override
  public void processModelChange(String attributeChanged, VehicleProcessModelTO newProcessModel) {
    if (!(newProcessModel instanceof ExampleProcessModelTO)) {
      return;
    }
    processModel = (ExampleProcessModelTO) newProcessModel;

    if (Objects.equals(attributeChanged,
                       VehicleProcessModel.Attribute.COMM_ADAPTER_ENABLED.name())) {
      updateCommAdapterEnabled(processModel.isCommAdapterEnabled());
    }
    else if (Objects.equals(attributeChanged,
                            VehicleProcessModel.Attribute.COMM_ADAPTER_CONNECTED.name())) {
      updateCommAdapterConnected(processModel.isCommAdapterConnected());
    }
    else if (Objects.equals(attributeChanged, ExampleProcessModel.Attribute.VEHICLE_HOST.name())) {
      updateVehicleHost(processModel.getVehicleHost());
    }
    else if (Objects.equals(attributeChanged, ExampleProcessModel.Attribute.VEHICLE_PORT.name())) {
      updateVehiclePort(processModel.getVehiclePort());
    }
    else if (Objects.equals(attributeChanged,
                            ExampleProcessModel.Attribute.VEHICLE_IDLE_TIMEOUT.name())) {
      updateVehicleTimeout(processModel.getVehicleIdleTimeout());
    }
    else if (Objects.equals(attributeChanged, ExampleProcessModel.Attribute.VEHICLE_IDLE.name())) {
      updateVehicleIdle(processModel.isVehicleIdle());
    }
    else if (Objects.equals(attributeChanged,
                            ExampleProcessModel.Attribute.DISCONNECTING_ON_IDLE.name())) {
      updateDisconnectingOnIdle(processModel.isDisconnectingOnVehicleIdle());
    }
    else if (Objects.equals(attributeChanged,
                            ExampleProcessModel.Attribute.RECONNECTING_ON_CONNECTION_LOSS.name())) {
      updateReconnectingOnConnectionLoss(processModel.isReconnectingOnConnectionLoss());
    }
    else if (Objects.equals(attributeChanged, ExampleProcessModel.Attribute.LAST_ORDER.name())) {
      updateLastOrder(processModel.getLastOrderSent(), processModel.isCommAdapterConnected());
    }
    else if (Objects.equals(attributeChanged,
                            ExampleProcessModel.Attribute.LOGGING_ENABLED.name())) {
      updateLoggingEnabled(processModel.isLoggingEnabled());
    }
  }

  /**
   * Updates the state of specific elements in the gui to let the user interact with them or not
   * depending on the enabled state of the comm adapter.
   *
   * @param enabled The enabled state of the comm adapter
   */
  private void updateCommAdapterEnabled(boolean enabled) {
    SwingUtilities.invokeLater(() -> {
      enableAdapterCheckBox.setSelected(enabled);

      hostTextField.setEditable(!enabled);
      portTextField.setEditable(!enabled);
      aliveTimeoutTextField.setEditable(!enabled);
      disconnectOnTimeoutChkBox.setEnabled(!enabled);
      reconnectOnConnectionLossChkBox.setEnabled(!enabled);
      enableLoggingChkBox.setEnabled(!enabled);
    });
  }

  /**
   * Updates buttons for interacting with the vehicle when the connection is established or not.
   *
   * @param connected Whether the connection to the vehicle is established
   */
  private void updateCommAdapterConnected(boolean connected) {
    SwingUtilities.invokeLater(() -> {
      connectedButton.setSelected(connected);

      sendOrderButton.setEnabled(connected);
      repeatLastOrderButton.setEnabled(connected);
    });
  }

  /**
   * Updates the shown host ip in the gui.
   *
   * @param host The host ip
   */
  private void updateVehicleHost(String host) {
    SwingUtilities.invokeLater(() -> hostTextField.setText(host));
  }

  /**
   * Updates the shown port in the gui
   *
   * @param port The port
   */
  private void updateVehiclePort(int port) {
    SwingUtilities.invokeLater(() -> portTextField.setText(Integer.toString(port)));
  }

  /**
   * Updates the shown idle timeout in the gui.
   *
   * @param timeout The idle timeout
   */
  private void updateVehicleTimeout(int timeout) {
    SwingUtilities.invokeLater(() -> aliveTimeoutTextField.setText(Integer.toString(timeout)));
  }

  /**
   * Updates the idle state of the vehicle in the gui.
   *
   * @param idle The idle state
   */
  private void updateVehicleIdle(boolean idle) {
    SwingUtilities.invokeLater(() -> activeButton.setSelected(!idle));
  }

  /**
   * Updates the checkbox on whether the connection should be closed when the vehicle is idle.
   *
   * @param disconnectingOnIdle Whether the connection should be closed if the vehicle is idle
   */
  private void updateDisconnectingOnIdle(boolean disconnectingOnIdle) {
    SwingUtilities.invokeLater(() -> disconnectOnTimeoutChkBox.setSelected(disconnectingOnIdle));
  }

  /**
   * Updates the checkbox on whether a connection should be reestablished after loss.
   *
   * @param reconnect Whether a connection should be reestablished after loss
   */
  private void updateReconnectingOnConnectionLoss(boolean reconnect) {
    SwingUtilities.invokeLater(() -> reconnectOnConnectionLossChkBox.setSelected(reconnect));
  }

  /**
   * Updates the list of last orders sent with the given one as last order.
   *
   * @param lastOrderSent The last order sent
   * @param connected The connection state to the vehicle with <code>true</code> indicates a
   * connection is established
   */
  private void updateLastOrder(OrderRequest lastOrderSent, boolean connected) {
    SwingUtilities.invokeLater(() -> {
      if (lastOrderSent != null) {
        OrderRequest selectedTelegram = lastOrdersList.getSelectedValue();
        if (selectedTelegram == null) {
          selectedTelegram = lastOrderSent;
        }

        updateLastOrderTextFields(selectedTelegram);
        repeatLastOrderButton.setEnabled(connected);

        lastOrderListModel.add(0, lastOrderSent);
        lastOrdersList.setSelectedIndex(0);
        // Delete last element if our listmodel contains too many elements
        while (lastOrderListModel.getSize() > MAX_LAST_ORDERS) {
          lastOrderListModel.removeElement(lastOrderListModel.lastElement());
        }
      }
    });
  }

  /**
   * Updates the checkbox on whether the comm adapter logging is enabled.
   *
   * @param enabled Whether the comm adapter logging is enabled
   */
  private void updateLoggingEnabled(boolean enabled) {
    SwingUtilities.invokeLater(() -> enableLoggingChkBox.setSelected(enabled));
  }

  /**
   * Sends a command to the comm adapter.
   *
   * @param command The command
   */
  private void sendAdapterCommand(AdapterCommand command) {
    try {
      callWrapper.call(() -> vehicleService.sendCommAdapterCommand(processModel.getVehicleRef(),
                                                                   command));
    }
    catch (Exception ex) {
      LOG.warn("Error sending comm adapter command '{}'", command, ex);
    }
  }

  /**
   * Enables or disables the comm adapter.
   *
   * @param enable Whether the comm adapter should be enabled or disabled
   */
  private void enableCommAdapter(boolean enable) {
    try {
      if (enable) {
        callWrapper.call(() -> vehicleService.enableCommAdapter(processModel.getVehicleRef()));
      }
      else {
        callWrapper.call(() -> vehicleService.disableCommAdapter(processModel.getVehicleRef()));
      }
    }
    catch (Exception ex) {
      LOG.warn("Error enabling/disabling comm adapter", ex);
    }
  }

  /**
   * This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  // Generated using JFormDesigner Evaluation license - Benoni Jiang
  private void initComponents() {
      ResourceBundle bundle = ResourceBundle.getBundle("de.fraunhofer.iml.opentcs.example.commadapter.vehicle.Bundle");
      connectionSettingsPanel = new JPanel();
      enableAdapterCheckBox = new JCheckBox();
      hostLabel = new JLabel();
      hostTextField = new JTextField();
      connectedButton = new JButton();
      portLabel = new JLabel();
      portTextField = new JTextField();
      activeButton = new JButton();
      aliveTimeoutLable = new JLabel();
      aliveTimeoutTextField = new JTextField();
      disconnectOnTimeoutChkBox = new JCheckBox();
      reconnectOnConnectionLossChkBox = new JCheckBox();
      enableLoggingChkBox = new JCheckBox();
      setOrderPanel = new JPanel();
      destinationLabel = new JLabel();
      destinationComboBox = new JComboBox<>();
      actionLabel = new JLabel();
      actionComboBox = new JComboBox<>();
      orderIdLabel = new JLabel();
      orderIdTextField = new JTextField();
      sendOrderButton = new JButton();
      repeatOrderPanel = new JPanel();
      lastOrdersScrollPane = new JScrollPane();
      lastOrdersList = new JList<>();
      lastOrderDetailsPanel = new JPanel();
      destinationLabel1 = new JLabel();
      lastDestinationTextField = new JTextField();
      actionLabel1 = new JLabel();
      lastActionTextField = new JTextField();
      lastOrderIdLabel = new JLabel();
      lastOrderIdTextField = new JTextField();
      repeatLastOrderButton = new JButton();

      //======== this ========
      setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder(
      0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder
      . BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ), java. awt. Color.
      red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .
      beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );

      //======== connectionSettingsPanel ========
      {
          connectionSettingsPanel.setBorder(new TitledBorder(null, "Connection to vehicle", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
              new Font("Tahoma", Font.BOLD, 11)));
          connectionSettingsPanel.setLayout(new GridBagLayout());

          //---- enableAdapterCheckBox ----
          enableAdapterCheckBox.setText(bundle.getString("EnableAdapter"));
          enableAdapterCheckBox.addActionListener(e -> enableAdapterCheckBoxActionPerformed(e));
          connectionSettingsPanel.add(enableAdapterCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.NONE,
              new Insets(0, 0, 0, 0), 0, 0));

          //---- hostLabel ----
          hostLabel.setText(bundle.getString("VehicleIpAddress"));
          connectionSettingsPanel.add(hostLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.EAST, GridBagConstraints.NONE,
              new Insets(0, 3, 0, 3), 0, 0));

          //---- hostTextField ----
          hostTextField.setColumns(12);
          hostTextField.setText("XXX.XXX.XXX.XXX");
          connectionSettingsPanel.add(hostTextField, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 3, 3), 0, 0));

          //---- connectedButton ----
          connectedButton.setIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDGray.gif")));
          connectedButton.setText(bundle.getString("AdapterConnected"));
          connectedButton.setBorderPainted(false);
          connectedButton.setDisabledIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDRed.gif")));
          connectedButton.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDGreen.gif")));
          connectedButton.setEnabled(false);
          connectionSettingsPanel.add(connectedButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 0, 0), 0, 0));

          //---- portLabel ----
          portLabel.setText(bundle.getString("VehicleTcpPort"));
          connectionSettingsPanel.add(portLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
              GridBagConstraints.EAST, GridBagConstraints.NONE,
              new Insets(0, 3, 0, 3), 0, 0));

          //---- portTextField ----
          portTextField.setDocument(new PlainDocument(){
              @Override
              public void insertString(int offs, String str, AttributeSet a)
                  throws BadLocationException {
                  try {
                      String oldString = getText(0, getLength());
                      StringBuilder newString = new StringBuilder(oldString);
                      newString.insert(offs, str);
                      int newValue = Integer.parseInt(newString.toString());
                      if (newValue >= 1 && newValue <= 65535) {
                          super.insertString(offs, str, a);
                      }
                  }
                  catch(NumberFormatException e) { }
              }
          }
          );
          portTextField.setColumns(6);
          portTextField.setText("XXXXX");
          connectionSettingsPanel.add(portTextField, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 3, 3), 0, 0));

          //---- activeButton ----
          activeButton.setIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDGray.gif")));
          activeButton.setText(bundle.getString("ControlPanel.AdapterActive"));
          activeButton.setBorderPainted(false);
          activeButton.setDisabledIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDRed.gif")));
          activeButton.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/de/fraunhofer/iml/opentcs/example/commadapter/vehicle/res/symbols/LEDGreen.gif")));
          activeButton.setEnabled(false);
          connectionSettingsPanel.add(activeButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 0, 0), 0, 0));

          //---- aliveTimeoutLable ----
          aliveTimeoutLable.setText(bundle.getString("ControlPanel.IdleAfter"));
          connectionSettingsPanel.add(aliveTimeoutLable, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
              GridBagConstraints.EAST, GridBagConstraints.NONE,
              new Insets(0, 3, 0, 3), 0, 0));

          //---- aliveTimeoutTextField ----
          aliveTimeoutTextField.setDocument(new PlainDocument(){
              @Override
              public void insertString(int offs, String str, AttributeSet a)
                  throws BadLocationException {
                  try {
                      Integer.parseInt(str);
                      super.insertString(offs, str, a);
                  }
                  catch(NumberFormatException e) { }
              }
          });
          aliveTimeoutTextField.setColumns(6);
          aliveTimeoutTextField.setText("XXXXX");
          connectionSettingsPanel.add(aliveTimeoutTextField, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 3, 3), 0, 0));

          //---- disconnectOnTimeoutChkBox ----
          disconnectOnTimeoutChkBox.setText(bundle.getString("ControlPanel.DisconnectOnTimeout"));
          disconnectOnTimeoutChkBox.addActionListener(e -> disconnectOnTimeoutChkBoxActionPerformed(e));
          connectionSettingsPanel.add(disconnectOnTimeoutChkBox, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 0, 0), 0, 0));

          //---- reconnectOnConnectionLossChkBox ----
          reconnectOnConnectionLossChkBox.setText(bundle.getString("ControlPanel.DisconnectOnConnectionLoss"));
          reconnectOnConnectionLossChkBox.addActionListener(e -> reconnectOnConnectionLossChkBoxActionPerformed(e));
          connectionSettingsPanel.add(reconnectOnConnectionLossChkBox, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 0, 0), 0, 0));

          //---- enableLoggingChkBox ----
          enableLoggingChkBox.setText(bundle.getString("ControlPanel.EnableLogging"));
          enableLoggingChkBox.addActionListener(e -> enableLoggingChkBoxActionPerformed(e));
          connectionSettingsPanel.add(enableLoggingChkBox, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 0, 0), 0, 0));
      }

      //======== setOrderPanel ========
      {
          setOrderPanel.setBorder(new TitledBorder(null, "New order telegram", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
              new Font("Tahoma", Font.BOLD, 11)));
          setOrderPanel.setLayout(new GridBagLayout());

          //---- destinationLabel ----
          destinationLabel.setText(bundle.getString("OrderDestination"));
          setOrderPanel.add(destinationLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.EAST, GridBagConstraints.NONE,
              new Insets(0, 3, 0, 3), 0, 0));

          //---- destinationComboBox ----
          destinationComboBox.setEditor(new TCSObjectComboBoxEditor());
          destinationComboBox.setRenderer(new StringListCellRenderer<Point>(point -> point.getName()));
          destinationComboBox.setEditable(true);
          setOrderPanel.add(destinationComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 3, 3), 0, 0));

          //---- actionLabel ----
          actionLabel.setText(bundle.getString("OrderAction"));
          setOrderPanel.add(actionLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
              GridBagConstraints.EAST, GridBagConstraints.NONE,
              new Insets(0, 3, 0, 3), 0, 0));
          setOrderPanel.add(actionComboBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 3, 3), 0, 0));

          //---- orderIdLabel ----
          orderIdLabel.setText(bundle.getString("OrderID"));
          setOrderPanel.add(orderIdLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
              GridBagConstraints.EAST, GridBagConstraints.NONE,
              new Insets(0, 3, 0, 3), 0, 0));

          //---- orderIdTextField ----
          orderIdTextField.setColumns(6);
          orderIdTextField.setText("1");
          setOrderPanel.add(orderIdTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
              GridBagConstraints.WEST, GridBagConstraints.NONE,
              new Insets(0, 0, 3, 3), 0, 0));

          //---- sendOrderButton ----
          sendOrderButton.setText(bundle.getString("SendNewOrder"));
          sendOrderButton.addActionListener(e -> sendOrderButtonActionPerformed(e));
          setOrderPanel.add(sendOrderButton, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
              new Insets(0, 3, 3, 3), 0, 0));
      }

      //======== repeatOrderPanel ========
      {
          repeatOrderPanel.setBorder(new TitledBorder(null, "Last orders sent to vehicle", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
              new Font("Tahoma", Font.BOLD, 11)));
          repeatOrderPanel.setMinimumSize(new Dimension(199, 170));

          //======== lastOrdersScrollPane ========
          {

              //---- lastOrdersList ----
              lastOrdersList.setModel(lastOrderListModel);
              lastOrdersList.setCellRenderer(new OrderListCellRenderer());
              lastOrdersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
              lastOrdersScrollPane.setViewportView(lastOrdersList);
          }

          //======== lastOrderDetailsPanel ========
          {
              lastOrderDetailsPanel.setLayout(new GridBagLayout());

              //---- destinationLabel1 ----
              destinationLabel1.setText(bundle.getString("OrderDestination"));
              lastOrderDetailsPanel.add(destinationLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                  GridBagConstraints.EAST, GridBagConstraints.NONE,
                  new Insets(0, 3, 0, 3), 0, 0));

              //---- lastDestinationTextField ----
              lastDestinationTextField.setEditable(false);
              lastDestinationTextField.setColumns(12);
              lastDestinationTextField.setHorizontalAlignment(SwingConstants.RIGHT);
              lastOrderDetailsPanel.add(lastDestinationTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                  GridBagConstraints.WEST, GridBagConstraints.NONE,
                  new Insets(0, 0, 3, 3), 0, 0));

              //---- actionLabel1 ----
              actionLabel1.setText(bundle.getString("OrderAction"));
              lastOrderDetailsPanel.add(actionLabel1, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0,
                  GridBagConstraints.EAST, GridBagConstraints.NONE,
                  new Insets(0, 3, 0, 3), 0, 0));

              //---- lastActionTextField ----
              lastActionTextField.setEditable(false);
              lastActionTextField.setColumns(12);
              lastActionTextField.setHorizontalAlignment(SwingConstants.RIGHT);
              lastOrderDetailsPanel.add(lastActionTextField, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0,
                  GridBagConstraints.WEST, GridBagConstraints.NONE,
                  new Insets(0, 0, 3, 3), 0, 0));

              //---- lastOrderIdLabel ----
              lastOrderIdLabel.setText(bundle.getString("OrderID"));
              lastOrderDetailsPanel.add(lastOrderIdLabel, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0,
                  GridBagConstraints.EAST, GridBagConstraints.NONE,
                  new Insets(0, 3, 0, 3), 0, 0));

              //---- lastOrderIdTextField ----
              lastOrderIdTextField.setEditable(false);
              lastOrderIdTextField.setColumns(6);
              lastOrderDetailsPanel.add(lastOrderIdTextField, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0,
                  GridBagConstraints.WEST, GridBagConstraints.NONE,
                  new Insets(0, 0, 3, 3), 0, 0));

              //---- repeatLastOrderButton ----
              repeatLastOrderButton.setText(bundle.getString("SendLastOrderAgain"));
              repeatLastOrderButton.setEnabled(false);
              repeatLastOrderButton.addActionListener(e -> repeatLastOrderButtonActionPerformed(e));
              lastOrderDetailsPanel.add(repeatLastOrderButton, new GridBagConstraints(0, 12, 2, 1, 0.0, 0.0,
                  GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                  new Insets(0, 3, 3, 3), 0, 0));
          }

          GroupLayout repeatOrderPanelLayout = new GroupLayout(repeatOrderPanel);
          repeatOrderPanel.setLayout(repeatOrderPanelLayout);
          repeatOrderPanelLayout.setHorizontalGroup(
              repeatOrderPanelLayout.createParallelGroup()
                  .addGroup(repeatOrderPanelLayout.createSequentialGroup()
                      .addComponent(lastOrdersScrollPane, GroupLayout.PREFERRED_SIZE, 166, GroupLayout.PREFERRED_SIZE)
                      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                      .addComponent(lastOrderDetailsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                      .addGap(0, 0, 0))
          );
          repeatOrderPanelLayout.setVerticalGroup(
              repeatOrderPanelLayout.createParallelGroup()
                  .addGroup(repeatOrderPanelLayout.createSequentialGroup()
                      .addGroup(repeatOrderPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                          .addComponent(lastOrdersScrollPane)
                          .addComponent(lastOrderDetailsPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                      .addGap(0, 0, 0))
          );
      }

      GroupLayout layout = new GroupLayout(this);
      setLayout(layout);
      layout.setHorizontalGroup(
          layout.createParallelGroup()
              .addGroup(layout.createSequentialGroup()
                  .addGap(0, 0, 0)
                  .addGroup(layout.createParallelGroup()
                      .addGroup(layout.createSequentialGroup()
                          .addComponent(connectionSettingsPanel, GroupLayout.PREFERRED_SIZE, 380, GroupLayout.PREFERRED_SIZE)
                          .addGap(6, 6, 6)
                          .addComponent(setOrderPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                      .addComponent(repeatOrderPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                  .addGap(0, 0, 0))
      );
      layout.setVerticalGroup(
          layout.createParallelGroup()
              .addGroup(layout.createSequentialGroup()
                  .addGap(0, 0, 0)
                  .addGroup(layout.createParallelGroup()
                      .addComponent(connectionSettingsPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                      .addComponent(setOrderPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                  .addGap(6, 6, 6)
                  .addComponent(repeatOrderPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
      );
  }// </editor-fold>//GEN-END:initComponents

  private void repeatLastOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeatLastOrderButtonActionPerformed
    // If there's only one old telegram send this
    OrderRequest lastOrderSent = processModel.getLastOrderSent();
    if (lastOrderSent != null && lastOrdersList.getModel().getSize() == 1) {
      sendAdapterCommand(new SendRequestCommand(lastOrderSent));
    }
    // Otherwise if there is an order selected send this
    else if (!lastOrdersList.isSelectionEmpty()) {
      sendAdapterCommand(new SendRequestCommand(lastOrdersList.getSelectedValue()));
    }
    else {
      // This should never happen
      JOptionPane.showMessageDialog(this, "There's no last order to repeat.");
    }
  }//GEN-LAST:event_repeatLastOrderButtonActionPerformed

  private void sendOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendOrderButtonActionPerformed
    // Parse and check destination
    Object selectedItem = destinationComboBox.getSelectedItem();
    String destinationIdString = selectedItem instanceof Point
        ? ((Point) selectedItem).getName() : selectedItem.toString();
    int destinationId = 0;
    try {
      destinationId = Integer.parseInt(destinationIdString);
      if (destinationId < 0 || destinationId > 65535) {
        throw new NumberFormatException("destinationId out of bounds");
      }
    }
    catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Destination ID must be in [0..65535].");
      return;
    }

    // Parse and check action
    OrderRequest.OrderAction action = (OrderRequest.OrderAction) actionComboBox.getSelectedItem();

    // Parse and check order ID.
    int orderId;
    try {
      orderId = Integer.parseUnsignedInt(orderIdTextField.getText());
      if (orderId < 0 || orderId > 65535) {
        throw new NumberFormatException("orderId out of bounds");
      }
    }
    catch (NumberFormatException exc) {
      JOptionPane.showMessageDialog(this, "Order ID must be in [0..65535].");
      return;
    }

    OrderRequest telegram = new OrderRequest(0,
                                             orderId,
                                             destinationId,
                                             action);
    // Send this telegram to vehicle.
    sendAdapterCommand(new SendRequestCommand(telegram));

    // Increment the order ID in case the user wants to send another one.
    orderId = (orderId + 1) % 65535;
    if (orderId == 0) {
      orderId = 1;
    }
    orderIdTextField.setText(Integer.toString(orderId));
  }//GEN-LAST:event_sendOrderButtonActionPerformed

  private void enableAdapterCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableAdapterCheckBoxActionPerformed
    if (enableAdapterCheckBox.isSelected()) {
      String host = hostTextField.getText();
      if (Strings.isNullOrEmpty(host)) {
        enableAdapterCheckBox.setSelected(false);
        JOptionPane.showMessageDialog(this, "IP address can not be empty.");
        return;
      }

      String portString = portTextField.getText();
      int port;
      if (!Strings.isNullOrEmpty(portString)) {
        port = Integer.valueOf(portString);
      }
      else {
        enableAdapterCheckBox.setSelected(false);
        JOptionPane.showMessageDialog(this, "TCP port can not be empty.");
        return;
      }

      String timeoutString = aliveTimeoutTextField.getText();
      int timeout;
      if (!Strings.isNullOrEmpty(timeoutString)) {
        timeout = Integer.valueOf(aliveTimeoutTextField.getText());
      }
      else {
        enableAdapterCheckBox.setSelected(false);
        JOptionPane.showMessageDialog(this, "Inactive after (ms) can not be empty.");
        return;
      }

      sendAdapterCommand(new SetVehicleHostCommand(host));
      sendAdapterCommand(new SetVehiclePortCommand(port));
      sendAdapterCommand(new SetIdleTimeoutCommand(timeout));
    }

    enableCommAdapter(enableAdapterCheckBox.isSelected());
  }//GEN-LAST:event_enableAdapterCheckBoxActionPerformed

  private void disconnectOnTimeoutChkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectOnTimeoutChkBoxActionPerformed
    sendAdapterCommand(new SetDisconnectingOnVehicleIdleCommand(disconnectOnTimeoutChkBox.isSelected()));
  }//GEN-LAST:event_disconnectOnTimeoutChkBoxActionPerformed

  private void reconnectOnConnectionLossChkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reconnectOnConnectionLossChkBoxActionPerformed
    sendAdapterCommand(new SetReconnectingOnConnectionLossCommand(reconnectOnConnectionLossChkBox.isSelected()));
  }//GEN-LAST:event_reconnectOnConnectionLossChkBoxActionPerformed

  private void enableLoggingChkBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enableLoggingChkBoxActionPerformed
    sendAdapterCommand(new SetLoggingEnabledCommand(enableLoggingChkBox.isSelected()));
  }//GEN-LAST:event_enableLoggingChkBoxActionPerformed

  /**
   * Updates the panel containing informations about the last order sent with the selected order
   * from the list.
   *
   * @param evt The selection event for the order list
   */
  private void lastOrdersSentValueChanged(javax.swing.event.ListSelectionEvent evt) {
    OrderRequest selectedTelegram = lastOrdersList.getSelectedValue();
    updateLastOrderTextFields(selectedTelegram);
  }

  /**
   * Updates the last order text fields with the content of the given order request.
   *
   * @param telegram The order request
   */
  private void updateLastOrderTextFields(OrderRequest telegram) {
    if (telegram != null) {
      lastDestinationTextField.setText(String.valueOf(telegram.getDestinationId()));
      lastActionTextField.setText(telegram.getDestinationAction().toString());
      lastOrderIdTextField.setText(Integer.toString(telegram.getId()));
      repeatLastOrderButton.setEnabled(processModel.isCommAdapterConnected());
    }
    else {
      lastDestinationTextField.setText("-");
      lastActionTextField.setText("-");
      lastOrderIdTextField.setText("-");
      repeatLastOrderButton.setEnabled(false);
    }
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  // Generated using JFormDesigner Evaluation license - Benoni Jiang
  private JPanel connectionSettingsPanel;
  private JCheckBox enableAdapterCheckBox;
  private JLabel hostLabel;
  private JTextField hostTextField;
  private JButton connectedButton;
  private JLabel portLabel;
  private JTextField portTextField;
  private JButton activeButton;
  private JLabel aliveTimeoutLable;
  private JTextField aliveTimeoutTextField;
  private JCheckBox disconnectOnTimeoutChkBox;
  private JCheckBox reconnectOnConnectionLossChkBox;
  private JCheckBox enableLoggingChkBox;
  private JPanel setOrderPanel;
  private JLabel destinationLabel;
  private JComboBox<Point> destinationComboBox;
  private JLabel actionLabel;
  private JComboBox<OrderRequest.OrderAction> actionComboBox;
  private JLabel orderIdLabel;
  private JTextField orderIdTextField;
  private JButton sendOrderButton;
  private JPanel repeatOrderPanel;
  private JScrollPane lastOrdersScrollPane;
  private JList<OrderRequest> lastOrdersList;
  private JPanel lastOrderDetailsPanel;
  private JLabel destinationLabel1;
  private JTextField lastDestinationTextField;
  private JLabel actionLabel1;
  private JTextField lastActionTextField;
  private JLabel lastOrderIdLabel;
  private JTextField lastOrderIdTextField;
  private JButton repeatLastOrderButton;
  // End of variables declaration//GEN-END:variables
}
