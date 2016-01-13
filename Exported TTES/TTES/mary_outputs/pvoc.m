function y = pvoc(x, r, n)

if nargin < 3
  n = 1024;
end

% With hann windowing on both input and output, 
% we need 25% window overlap for smooth reconstruction
hop = n/4;

scf = 1.0;

% Calculate the basic STFT, magnitude scaled
X = scf * stft(x', n, n, hop);

% Calculate the new timebase samples
[rows, cols] = size(X);
t = 0:r:(cols-2);

% Generate the new spectrogram
X2 = pvsample(X, t, hop);

% Invert to a waveform
y = istft(X2, n, n, hop)';
