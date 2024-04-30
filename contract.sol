pragma solidity ^0.8.15;
// SPDX-License-Identifier: MIT


contract EtherSender {
    // Event to log the ether transfer
    event EtherTransferred(address indexed _sender, address indexed _receiver, uint256 _amount);

    // Function to send ether from sender to receiver
    function sendEther(address payable _receiver) external payable {
        // Ensure that the receiver address is valid
        require(_receiver != address(0), "Invalid receiver address");

        // Ensure that the amount sent is greater than 0
        require(msg.value > 0, "Amount must be greater than 0");

        // Transfer ether to the receiver
        _receiver.transfer(msg.value);

        // Emit event to log the transfer
        emit EtherTransferred(msg.sender, _receiver, msg.value);
    }
}