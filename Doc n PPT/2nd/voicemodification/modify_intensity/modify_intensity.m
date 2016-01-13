clear all; close all; clc;
[s, fs]=wavread('Hereadbook.wav');
sound(s,fs);

plot(s);
 % define common parameters
                                             % sampling frequency (Hz)
    duration = 60;                          % signal duration (ms)
    N = floor(duration*1E-3*fs);            % signal length (samples)
                  
                                            % frequency vector (Hz)
%window = @hanning;                      % analysis window function
%   window = @(N)( chebwin(N,40) );         % analysis window function
    window = @(N)( chebwin(N,100) );        % analysis window function
 

    
    signal=fade(s,fs,duration,window);
    %sound(signal,fs);
    plot(signal)
    co=s.*0.5;
    sound(co,fs);
    
    
    
    
    