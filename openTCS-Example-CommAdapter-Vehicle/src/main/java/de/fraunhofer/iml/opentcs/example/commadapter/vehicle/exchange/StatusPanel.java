/**
 * Copyright (c) Fraunhofer IML
 */
package de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import com.google.inject.assistedinject.Assisted;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.ExampleProcessModel;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.commands.SendRequestCommand;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.commands.SetPeriodicStateRequestEnabledCommand;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.exchange.commands.SetStateRequestIntervalCommand;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateRequest;
import de.fraunhofer.iml.opentcs.example.commadapter.vehicle.telegrams.StateResponse;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.opentcs.components.kernel.services.VehicleService;
import org.opentcs.customizations.ServiceCallWrapper;
import org.opentcs.drivers.vehicle.AdapterCommand;
import org.opentcs.drivers.vehicle.VehicleProcessModel;
import org.opentcs.drivers.vehicle.management.VehicleCommAdapterPanel;
import org.opentcs.drivers.vehicle.management.VehicleProcessModelTO;
import org.opentcs.util.CallWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author Mats Wilhelm (Fraunhofer IML)
 */
public class StatusPanel
    extends VehicleCommAdapterPanel {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StatusPanel.class);
  /**
   * The bundle to use.
   */
  private static final ResourceBundle resourceBundle 
      = ResourceBundle.getBundle("de/fraunhofer/iml/opentcs/example/commadapter/vehicle/Bundle");
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
   * @param processModel The comm adapter's process model.
   * @param vehicleService The vehicle service.
   * @param callWrapper The call wrapper to use for service calls.
   */
  @Inject
  public StatusPanel(@Assisted ExampleProcessModelTO processModel,
                     @Assisted VehicleService vehicleService,
                     @ServiceCallWrapper CallWrapper callWrapper) {
    this.processModel = requireNonNull(processModel, "processModel");
    this.vehicleService = requireNonNull(vehicleService, "vehicleService");
    this.callWrapper = requireNonNull(callWrapper, "callWrapper");

    initComponents();
    initGuiContent();
  }

  /**
   * Sets the initial content for each attribute of the process model.
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
                       VehicleProcessModel.Attribute.COMM_ADAPTER_CONNECTED.name())) {
      updateCommAdapterConnected(processModel.isCommAdapterConnected());
    }
    else if (Objects.equals(attributeChanged,
                            ExampleProcessModel.Attribute.CURRENT_STATE.name())) {
      updateCurrentState(processModel.getCurrentState());
    }
    else if (Objects.equals(attributeChanged,
                            ExampleProcessModel.Attribute.PERIODIC_STATE_REQUESTS_ENABLED.name())) {
      updatePeriodicStateRequestsEnabled(processModel.isPeriodicStateRequestEnabled(),
                                         processModel.isCommAdapterConnected());
    }
    else if (Objects.equals(attributeChanged,
                            ExampleProcessModel.Attribute.PERIOD_STATE_REQUESTS_INTERVAL.name())) {
      updateStateRequestInterval(processModel.getStateRequestInterval());
    }
  }

  /**
   * Updates the status panel when the connection to the vehicle changes.
   *
   * @param connected Whether a connection to the vehicle is established
   */
  private void updateCommAdapterConnected(boolean connected) {
    buttonGetState.setEnabled(connected);
  }

  /**
   * Updates the status panel with the given state response.
   *
   * @param stateTelegram The state response
   */
  private void updateCurrentState(StateResponse stateTelegram) {
    SwingUtilities.invokeLater(() -> {
      updateStatusPanel(stateTelegram);
    });
  }

  /**
   * Updates checkboxes to reflect changes on the state of periodic state request.
   *
   * @param requestsEnabled Whether periodic requests are enabled
   * @param connected Whether a connection to the vehicle is established
   */
  private void updatePeriodicStateRequestsEnabled(boolean requestsEnabled, boolean connected) {
    SwingUtilities.invokeLater(() -> {
      chkBoxEnablePeriodicGetState.setSelected(requestsEnabled);
      txtGetStateInterval.setEditable(!requestsEnabled);
      buttonGetState.setEnabled(!requestsEnabled && connected);
    });
  }

  /**
   * Updates the get state interval textfield with the given interval.
   *
   * @param interval The new interval
   */
  private void updateStateRequestInterval(int interval) {
    SwingUtilities.invokeLater(() -> txtGetStateInterval.setText(Integer.toString(interval)));
  }

  /**
   * Updates the status panel with the content of a state response.
   *
   * @param state The state response
   */
  private void updateStatusPanel(final StateResponse state) {
    requireNonNull(state, "state");
    textFieldStateTeleCount.setText(String.valueOf(state.getCurrentOrderId()));
    textFieldStateOS.setText(String.valueOf(state.getOperationState()));
    textFieldStateLS.setText(String.valueOf(state.getLoadState()));
    textFieldStateCRP.setText(String.valueOf(state.getPositionId()));
    textFieldLastOrderID.setText(String.valueOf(state.getLastFinishedOrderId()));
    textFieldCurrOrderID.setText(String.valueOf(state.getCurrentOrderId()));
    textFieldLastRcvdOrderID.setText(String.valueOf(state.getLastReceivedOrderId()));
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
      panelPeriodicStateRequests = new JPanel();
      chkBoxEnablePeriodicGetState = new JCheckBox();
      labelGetStateInterval = new JLabel();
      txtGetStateInterval = new JTextField();
      panelManualStateRequest = new JPanel();
      buttonGetState = new JButton();
      panelState = new JPanel();
      panelTelegramContent = new JPanel();
      labelStateTeleCount = new JLabel();
      textFieldStateTeleCount = new JTextField();
      labelStateOS = new JLabel();
      textFieldStateOS = new JTextField();
      labelStateLS = new JLabel();
      textFieldStateLS = new JTextField();
      labelStateCRP = new JLabel();
      textFieldStateCRP = new JTextField();
      fillingLabel2 = new JLabel();
      labelLastOrderID = new JLabel();
      textFieldLastOrderID = new JTextField();
      labelCurrOrderID = new JLabel();
      textFieldCurrOrderID = new JTextField();
      labelLastRcvdOrderID = new JLabel();
      textFieldLastRcvdOrderID = new JTextField();
      fillingLabel1 = new JLabel();

      //======== this ========
      setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(
      new javax.swing.border.EmptyBorder(0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn"
      ,javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM
      ,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,12)
      ,java.awt.Color.red), getBorder())); addPropertyChangeListener(
      new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e
      ){if("\u0062ord\u0065r".equals(e.getPropertyName()))throw new RuntimeException()
      ;}});
      setLayout(new GridBagLayout());

      //======== panelPeriodicStateRequests ========
      {
          panelPeriodicStateRequests.setBorder(new TitledBorder(null, "Periodic state requests", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
              new Font("Tahoma", Font.BOLD, 11)));
          panelPeriodicStateRequests.setMaximumSize(new Dimension(2147483647, 70));
          panelPeriodicStateRequests.setLayout(new GridBagLayout());

          //---- chkBoxEnablePeriodicGetState ----
          chkBoxEnablePeriodicGetState.setSelected(true);
          chkBoxEnablePeriodicGetState.setText(bundle.getString("EnablePeriodicStateRequests"));
          chkBoxEnablePeriodicGetState.addActionListener(e -> chkBoxEnablePeriodicGetStateActionPerformed(e));
          panelPeriodicStateRequests.add(chkBoxEnablePeriodicGetState, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.BOTH,
              new Insets(0, 0, 0, 0), 0, 0));

          //---- labelGetStateInterval ----
          labelGetStateInterval.setText(bundle.getString("StateRequestInterval"));
          panelPeriodicStateRequests.add(labelGetStateInterval, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
              GridBagConstraints.CENTER, GridBagConstraints.NONE,
              new Insets(0, 3, 0, 0), 0, 0));

          //---- txtGetStateInterval ----
          txtGetStateInterval.setEditable(false);
          txtGetStateInterval.setColumns(4);
          txtGetStateInterval.setHorizontalAlignment(SwingConstants.RIGHT);
          txtGetStateInterval.setText("500");
          txtGetStateInterval.setMinimumSize(new Dimension(38, 20));
          panelPeriodicStateRequests.add(txtGetStateInterval, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
              GridBagConstraints.EAST, GridBagConstraints.NONE,
              new Insets(0, 3, 0, 3), 0, 0));
      }
      add(panelPeriodicStateRequests, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 0), 0, 0));

      //======== panelManualStateRequest ========
      {
          panelManualStateRequest.setBorder(new TitledBorder(null, "Manual state requests", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
              new Font("Tahoma", Font.BOLD, 11)));
          panelManualStateRequest.setLayout(new CardLayout());

          //---- buttonGetState ----
          buttonGetState.setText(bundle.getString("SendStateRequest"));
          buttonGetState.setEnabled(false);
          buttonGetState.addActionListener(e -> buttonGetStateActionPerformed(e));
          panelManualStateRequest.add(buttonGetState, "card2");
      }
      add(panelManualStateRequest, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 0), 0, 0));

      //======== panelState ========
      {
          panelState.setBorder(new EmptyBorder(1, 1, 1, 1));
          panelState.setFont(new Font("Arial", Font.PLAIN, 11));
          panelState.setLayout(new BorderLayout());

          //======== panelTelegramContent ========
          {
              panelTelegramContent.setBorder(new TitledBorder(null, "Last reported state", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
                  new Font("Tahoma", Font.BOLD, 11)));
              panelTelegramContent.setLayout(new GridBagLayout());

              //---- labelStateTeleCount ----
              labelStateTeleCount.setText(bundle.getString("OrderID"));
              panelTelegramContent.add(labelStateTeleCount, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                  GridBagConstraints.EAST, GridBagConstraints.NONE,
                  new Insets(0, 0, 0, 3), 0, 0));

              //---- textFieldStateTeleCount ----
              textFieldStateTeleCount.setEditable(false);
              textFieldStateTeleCount.setBackground(new Color(255, 255, 204));
              textFieldStateTeleCount.setColumns(6);
              textFieldStateTeleCount.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
              textFieldStateTeleCount.setHorizontalAlignment(SwingConstants.TRAILING);
              textFieldStateTeleCount.setText("00000");
              panelTelegramContent.add(textFieldStateTeleCount, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                  GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                  new Insets(0, 0, 3, 0), 0, 0));

              //---- labelStateOS ----
              labelStateOS.setText(bundle.getString("OperationState"));
              panelTelegramContent.add(labelStateOS, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                  GridBagConstraints.EAST, GridBagConstraints.NONE,
                  new Insets(0, 0, 0, 3), 0, 0));

              //---- textFieldStateOS ----
              textFieldStateOS.setEditable(false);
              textFieldStateOS.setBackground(new Color(255, 255, 204));
              textFieldStateOS.setColumns(1);
              textFieldStateOS.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
              textFieldStateOS.setHorizontalAlignment(SwingConstants.TRAILING);
              textFieldStateOS.setText("X");
              panelTelegramContent.add(textFieldStateOS, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                  GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                  new Insets(0, 0, 3, 0), 0, 0));

              //---- labelStateLS ----
              labelStateLS.setText(bundle.getString("LoadState"));
              panelTelegramContent.add(labelStateLS, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                  GridBagConstraints.WEST, GridBagConstraints.NONE,
                  new Insets(0, 6, 0, 3), 0, 0));

              //---- textFieldStateLS ----
              textFieldStateLS.setEditable(false);
              textFieldStateLS.setBackground(new Color(255, 255, 204));
              textFieldStateLS.setColumns(1);
              textFieldStateLS.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
              textFieldStateLS.setHorizontalAlignment(SwingConstants.TRAILING);
              textFieldStateLS.setText("X");
              panelTelegramContent.add(textFieldStateLS, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                  GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                  new Insets(0, 0, 3, 0), 0, 0));

              //---- labelStateCRP ----
              labelStateCRP.setText(bundle.getString("currentPoint"));
              panelTelegramContent.add(labelStateCRP, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                  GridBagConstraints.EAST, GridBagConstraints.NONE,
                  new Insets(0, 6, 0, 3), 0, 0));

              //---- textFieldStateCRP ----
              textFieldStateCRP.setEditable(false);
              textFieldStateCRP.setBackground(new Color(255, 255, 204));
              textFieldStateCRP.setColumns(9);
              textFieldStateCRP.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
              textFieldStateCRP.setHorizontalAlignment(SwingConstants.TRAILING);
              textFieldStateCRP.setText("CURPOINT");
              panelTelegramContent.add(textFieldStateCRP, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                  GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                  new Insets(0, 0, 3, 0), 0, 0));
              panelTelegramContent.add(fillingLabel2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                  GridBagConstraints.CENTER, GridBagConstraints.NONE,
                  new Insets(0, 0, 0, 0), 0, 0));

              //---- labelLastOrderID ----
              labelLastOrderID.setText(bundle.getString("LastFinishedOrder"));
              panelTelegramContent.add(labelLastOrderID, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                  GridBagConstraints.EAST, GridBagConstraints.NONE,
                  new Insets(0, 0, 3, 3), 0, 0));

              //---- textFieldLastOrderID ----
              textFieldLastOrderID.setEditable(false);
              textFieldLastOrderID.setBackground(new Color(255, 255, 204));
              textFieldLastOrderID.setColumns(6);
              textFieldLastOrderID.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
              textFieldLastOrderID.setHorizontalAlignment(SwingConstants.TRAILING);
              textFieldLastOrderID.setText("00000");
              panelTelegramContent.add(textFieldLastOrderID, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                  GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                  new Insets(0, 0, 3, 0), 0, 0));

              //---- labelCurrOrderID ----
              labelCurrOrderID.setText(bundle.getString("currentOrderID"));
              panelTelegramContent.add(labelCurrOrderID, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                  GridBagConstraints.WEST, GridBagConstraints.NONE,
                  new Insets(0, 6, 0, 3), 0, 0));

              //---- textFieldCurrOrderID ----
              textFieldCurrOrderID.setEditable(false);
              textFieldCurrOrderID.setBackground(new Color(255, 255, 204));
              textFieldCurrOrderID.setColumns(6);
              textFieldCurrOrderID.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
              textFieldCurrOrderID.setHorizontalAlignment(SwingConstants.TRAILING);
              textFieldCurrOrderID.setText("11111");
              panelTelegramContent.add(textFieldCurrOrderID, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                  GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                  new Insets(0, 0, 3, 0), 0, 0));

              //---- labelLastRcvdOrderID ----
              labelLastRcvdOrderID.setText(bundle.getString("lastReceivedOrderID"));
              panelTelegramContent.add(labelLastRcvdOrderID, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                  GridBagConstraints.EAST, GridBagConstraints.NONE,
                  new Insets(0, 6, 0, 3), 0, 0));

              //---- textFieldLastRcvdOrderID ----
              textFieldLastRcvdOrderID.setEditable(false);
              textFieldLastRcvdOrderID.setBackground(new Color(255, 255, 204));
              textFieldLastRcvdOrderID.setColumns(6);
              textFieldLastRcvdOrderID.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
              textFieldLastRcvdOrderID.setHorizontalAlignment(SwingConstants.TRAILING);
              textFieldLastRcvdOrderID.setText("22222");
              panelTelegramContent.add(textFieldLastRcvdOrderID, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
                  GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
                  new Insets(0, 0, 3, 0), 0, 0));
          }
          panelState.add(panelTelegramContent, BorderLayout.CENTER);
      }
      add(panelState, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 0), 0, 0));

      //---- fillingLabel1 ----
      fillingLabel1.setFont(new Font("Arial", Font.PLAIN, 11));
      add(fillingLabel1, new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0,
          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
          new Insets(0, 0, 0, 0), 0, 0));
  }// </editor-fold>//GEN-END:initComponents

    private void chkBoxEnablePeriodicGetStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxEnablePeriodicGetStateActionPerformed
      if (chkBoxEnablePeriodicGetState.isSelected()) {
        try {
          txtGetStateInterval.setEditable(false);
          int interval = Integer.parseInt(txtGetStateInterval.getText());
          if (interval <= 0) {
            throw new NumberFormatException();
          }
          sendAdapterCommand(new SetStateRequestIntervalCommand(interval));
          sendAdapterCommand(new SetPeriodicStateRequestEnabledCommand(true));
        }
        catch (NumberFormatException exc) {
          // Reset check box and text field.
          chkBoxEnablePeriodicGetState.setSelected(false);
          txtGetStateInterval.setEditable(true);
          // Notify the user about the invalid input.
          JOptionPane.showMessageDialog(this, resourceBundle.getString("InvalidRequestIntervalMsg"));
        }
      }
      else {
        sendAdapterCommand(new SetPeriodicStateRequestEnabledCommand(false));
        txtGetStateInterval.setEditable(true);
      }
    }//GEN-LAST:event_chkBoxEnablePeriodicGetStateActionPerformed

  private void buttonGetStateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGetStateActionPerformed
    SendRequestCommand command = new SendRequestCommand(new StateRequest(0));
    sendAdapterCommand(command);
  }//GEN-LAST:event_buttonGetStateActionPerformed

  private void sendAdapterCommand(AdapterCommand command) {
    try {
      callWrapper.call(() -> vehicleService.sendCommAdapterCommand(processModel.getVehicleRef(),
                                                                   command));
    }
    catch (Exception ex) {
      LOG.warn("Error sending comm adapter command '{}'", command, ex);
    }
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  // Generated using JFormDesigner Evaluation license - Benoni Jiang
  private JPanel panelPeriodicStateRequests;
  private JCheckBox chkBoxEnablePeriodicGetState;
  private JLabel labelGetStateInterval;
  private JTextField txtGetStateInterval;
  private JPanel panelManualStateRequest;
  private JButton buttonGetState;
  private JPanel panelState;
  private JPanel panelTelegramContent;
  private JLabel labelStateTeleCount;
  private JTextField textFieldStateTeleCount;
  private JLabel labelStateOS;
  private JTextField textFieldStateOS;
  private JLabel labelStateLS;
  private JTextField textFieldStateLS;
  private JLabel labelStateCRP;
  private JTextField textFieldStateCRP;
  private JLabel fillingLabel2;
  private JLabel labelLastOrderID;
  private JTextField textFieldLastOrderID;
  private JLabel labelCurrOrderID;
  private JTextField textFieldCurrOrderID;
  private JLabel labelLastRcvdOrderID;
  private JTextField textFieldLastRcvdOrderID;
  private JLabel fillingLabel1;
  // End of variables declaration//GEN-END:variables
}
